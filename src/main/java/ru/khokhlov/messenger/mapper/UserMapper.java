package ru.khokhlov.messenger.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.utils.PasswordEncoder;

import java.sql.Timestamp;
import java.time.ZoneId;


@Mapper(componentModel = "spring", imports = {PasswordEncoder.class, Timestamp.class, ZoneId.class})
public interface UserMapper {
    @Mapping(target = "password",
            expression = "java(PasswordEncoder.getEncryptedPassword(dto.password()))")
    User fromRegistrationDTOToUser(RegistrationFormDTO dto);

    @Mapping(target = "birthday",
            expression = "java(user.getBirthday()!=null ? LocalDate.ofInstant(user.getBirthday().toInstant() , ZoneId.systemDefault()) : null)")
    UserResponse fromUserToUSerResponse(User user);
}
