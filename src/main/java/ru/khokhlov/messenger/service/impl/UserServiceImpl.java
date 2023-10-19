package ru.khokhlov.messenger.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.khokhlov.messenger.dto.request.BasicUserInformation;
import ru.khokhlov.messenger.dto.request.NewPassword;
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.entity.Role;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.exception.ErrorMessage;
import ru.khokhlov.messenger.exception.InvalidDataException;
import ru.khokhlov.messenger.mapper.UserMapper;
import ru.khokhlov.messenger.repository.UserRepository;
import ru.khokhlov.messenger.service.MailSender;
import ru.khokhlov.messenger.service.RoleService;
import ru.khokhlov.messenger.service.UserService;
import ru.khokhlov.messenger.utils.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    @Value(value = "${application.database.delete.users.days}")
    private int deleteDay;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final MailSender mailSender;

    @Override
    public UserResponse createUser(RegistrationFormDTO userRegistrationInfo) throws EntityExistsException {
        log.trace("UserServiceImpl.createUser - userRegistrationInfo {}", userRegistrationInfo);

        if (userRepository.findByEmail(userRegistrationInfo.email()) != null) {
            throw new EntityExistsException("User with email: " + userRegistrationInfo.email() + " already exists!");
        }
        if (userRepository.findByNickname(userRegistrationInfo.nickname()) != null) {
            throw new EntityExistsException("User with nickname: " + userRegistrationInfo.nickname() + " already exists!");
        }

        User user = userMapper.fromRegistrationDTOToUser(userRegistrationInfo);
        user.setActivationCode(generateActivationCode());
        user.setRole(roleService.getRoleByName("USER"));
        userRepository.saveAndFlush(user);

        mailSender.activateEmail(user);

        log.trace("UserServiceImpl.createUser - user {}", user);
        return userMapper.fromUserToUSerResponse(user);
    }

    @Override
    public User getUserByNickname(String nickname) throws EntityNotFoundException {
        log.trace("UserServiceImpl.getUserByNickname - nickname {}", nickname);
        User user = userRepository.findByNickname(nickname);

        if (user == null){
            throw new EntityNotFoundException("User with nickname " + nickname + "not found");
        }

        log.trace("UserServiceImpl.getUserByNickname - user {}", user);
        return user;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String nickname) throws EntityNotFoundException {
        log.trace("UserServiceImpl.loadUserByUsername - nickname {}", nickname);
        User user = userRepository.findByNickname(nickname);

        if (user == null ){
            throw new EntityNotFoundException("User with nickname " + nickname + "not found");
        }

        List<Role> roleList = new ArrayList<>();
        roleList.add(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getNickname(),
                user.getPassword(),
                roleList.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }

    @Override
    public UserResponse updateUserInformation(String nickname, BasicUserInformation userInfo) throws EntityNotFoundException {
        log.trace("UserServiceImpl.createUser - userInfo {}", userInfo);

        User user = userRepository.findByNickname(nickname);

        userMapper.updateUser(userInfo,user);

        if (!Objects.equals(userInfo.email(), user.getEmail())) {
            user.setActivationCode(generateActivationCode());
            mailSender.activateEmail(user);
        }

        userRepository.saveAndFlush(user);

        log.trace("UserServiceImpl.createUser - user {}", user);
        return userMapper.fromUserToUSerResponse(user);
    }

    @Override
    public UserResponse updateUserPassword(String nickname, NewPassword password) throws InvalidDataException {
        log.trace("UserServiceImpl.updateUserPassword - password {}", password);

        User user = userRepository.findByNickname(nickname);

        if (PasswordEncoder.arePasswordsEquals(password.oldPassword(), user.getPassword())) {
            throw new InvalidDataException(new ErrorMessage("Old Password", "Incorrect old password"));
        }

        if (!Objects.equals(password.newPassword(), password.newConfirmedPassword())) {
            throw new InvalidDataException(new ErrorMessage("New password", "New password and new confirmed password are not equal!"));
        }

        user.setPassword(PasswordEncoder.getEncryptedPassword(password.newPassword()));
        userRepository.saveAndFlush(user);

        log.trace("UserServiceImpl.updateUserPassword - user {}", user);
        return userMapper.fromUserToUSerResponse(user);
    }

    @Override
    public UserResponse deleteUser(UserDTO userDTO) throws EntityNotFoundException {
        log.trace("UserServiceImpl.deleteUser - userDTO {}", userDTO);

        User user = checkUser(userDTO);

        user.setAccountActivity(false);
        user.setDeleteTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.saveAndFlush(user);

        log.trace("UserServiceImpl.deleteUser - user {}", user);
        return userMapper.fromUserToUSerResponse(user);
    }

    @Override
    public UserResponse restoreUser(UserDTO userDTO) throws EntityNotFoundException {
        log.trace("UserServiceImpl.restoreUser - userDTO {}", userDTO);

        User user = checkUser(userDTO);
        user.setAccountActivity(true);
        userRepository.saveAndFlush(user);

        log.trace("UserServiceImpl.restoreUser - user {}", user);
        return userMapper.fromUserToUSerResponse(user);
    }

    @Override
    public int deleteConfirmation() {
        List<User> userList = userRepository.findAllByAccountActivity(false);
        log.trace("UserServiceImpl.deleteConfirmation - userList {}", userList);

        List<User> usersToDelete = new ArrayList<>();
        LocalDateTime deleteDate = LocalDateTime.now().minusDays(deleteDay);

        for (User user : userList) {
            if (deleteDate.isAfter(user.getDeleteTimestamp().toLocalDateTime())) {
                usersToDelete.add(user);
            }
        }

        if (!userList.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
        }

        log.trace("UserServiceImpl.deleteConfirmation - usersToDelete {}", userList);
        return usersToDelete.size();
    }


    @Override
    public boolean isEmailActivate(String code) throws EntityNotFoundException {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            throw new EntityNotFoundException("This code does not exist!");
        }

        user.setActivationCode(null);
        userRepository.saveAndFlush(user);

        return true;
    }

    private User checkUser(UserDTO userDTO) {
        User user = userRepository.findByNickname(userDTO.nickname());

        if (user == null) {
            throw new EntityNotFoundException("User with nickname "
                    + userDTO.nickname()
                    + " not found!");
        }

        if (PasswordEncoder.arePasswordsEquals(userDTO.password(), user.getPassword())){
            throw new InvalidDataException(new ErrorMessage("Password", "Wrong password!"));
        }

        return user;
    }

    private String generateActivationCode() {
        log.debug("UserServiceImpl.generateActivationCode");

        String activationCode = "";
        boolean isCodeUniq = false;

        while (!isCodeUniq) {
            activationCode = UUID.randomUUID().toString();

            if (userRepository.findByActivationCode(activationCode) == null)
                isCodeUniq = true;
        }

        log.debug("UserServiceImpl.generateActivationCode - activationCode {}", activationCode);
        return activationCode;
    }
}
