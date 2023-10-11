package ru.khokhlov.messenger.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.khokhlov.messenger.dto.request.BasicUserInformation;
import ru.khokhlov.messenger.dto.request.NewPassword;
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.exception.ErrorMessage;
import ru.khokhlov.messenger.exception.InvalidDataException;
import ru.khokhlov.messenger.mapper.UserMapper;
import ru.khokhlov.messenger.repository.UserRepository;
import ru.khokhlov.messenger.service.MailSender;
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
    private final MailSender mailSender;

    @Override
    public UserResponse createUser(RegistrationFormDTO userRegistrationInfo) throws EntityExistsException {
        log.trace("UserServiceImpl.createUser - userRegistrationInfo {}", userRegistrationInfo);

        if (userRepository.findByEmail(userRegistrationInfo.email()) != null) {
            throw new EntityExistsException("User with email: " + userRegistrationInfo.email() + " already exist!");
        }
        if (userRepository.findByNickname(userRegistrationInfo.nickname()) != null) {
            throw new EntityExistsException("User with nickname: " + userRegistrationInfo.nickname() + " already exist!");
        }

        User user = userMapper.fromRegistrationDTOToUser(userRegistrationInfo);
        user.setActivationCode(generateActivationCode());
        user.setAccountActivity(true);
        userRepository.saveAndFlush(user);

        mailSender.activateEmail(user);

        log.trace("UserServiceImpl.createUser - user {}", user);
        return userMapper.fromUserToUSerResponse(user);
    }

    @Override
    public UserResponse updateUserInformation(BasicUserInformation userInfo) throws EntityNotFoundException {
        log.trace("UserServiceImpl.createUser - userInfo {}", userInfo);

        User user = userRepository.getReferenceById(userInfo.userId());
        updateUserBasicInfo(user, userInfo);
        userRepository.saveAndFlush(user);

        log.trace("UserServiceImpl.createUser - user {}", user);
        return userMapper.fromUserToUSerResponse(user);
    }

    @Override
    public UserResponse updateUserPassword(NewPassword password) throws InvalidDataException {
        log.trace("UserServiceImpl.updateUserPassword - password {}", password);

        User user = userRepository.getReferenceById(password.userId());

        if (!PasswordEncoder.isPasswordsAreEquals(password.oldPassword(), user.getPassword())) {
            throw new InvalidDataException(new ErrorMessage("Old Password", "Incorrect old password"));
        }

        if (!Objects.equals(password.newPassword(), password.newConfirmedPassword())) {
            throw new InvalidDataException(new ErrorMessage("New password", "New password and new confirmed password not equals!"));
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

        log.trace("UserServiceImpl.deleteConfirmation - usersToDelete {}", userList);
        if (!userList.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
        }

        return usersToDelete.size();
    }


    @Override
    public boolean isEmailActivate(String code) throws EntityNotFoundException {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            throw new EntityNotFoundException("This code is not exists!");
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

        if (!PasswordEncoder.isPasswordsAreEquals(userDTO.password(), user.getPassword())){
            throw new InvalidDataException(new ErrorMessage("Password", "Wrong password!"));
        }

        return user;
    }

    private void updateUserBasicInfo(User userFromDB, BasicUserInformation userInfo) {
        log.trace("UserServiceImpl.checkPassword - userFromDB {}, userInfo {}", userFromDB, userInfo);

        if (!Objects.equals(userFromDB.getEmail(), userInfo.email())) {
            mailSender.activateEmail(userFromDB);
            userFromDB.setEmail(userInfo.email());
        }

        userFromDB.setNickname(userInfo.nickname());
        userFromDB.setFirstName(userInfo.firstName());
        userFromDB.setLastName(userInfo.lastName());

        if (userInfo.middleName() != null) {
            userFromDB.setMiddleName(userInfo.middleName());
        }

        userFromDB.setBirthday(Timestamp.valueOf(userInfo.birthday().atStartOfDay()));
        userFromDB.setStatus(userInfo.status());

        log.trace("UserServiceImpl.checkPassword - user {}", userFromDB);
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

    @Override
    public UserResponse authUser(RegistrationFormDTO registrationFormDTO, String token) throws EntityNotFoundException {
        log.trace("UserServiceImpl.authUser - authFormDTO {}, token {}", registrationFormDTO, token);

        User user = userRepository.findByNicknameAndPassword(registrationFormDTO.email(), registrationFormDTO.password());
        return null;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, EntityNotFoundException {
        log.trace("UserServiceImpl.loadUserByUsername - username {}", username);
        User user;

        try {
            user = userRepository.findByNickname(username);
        } catch (Exception exception) {
            throw new EntityNotFoundException("User with email " + username + "not found");
        }

        return null;

//        return new User(
//                user.getEmail(),
//                user.getPassword(),
//                Collections.singleton(user.getRoleEnum())
//        );
    }
}
