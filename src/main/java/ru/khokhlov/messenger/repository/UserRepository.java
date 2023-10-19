package ru.khokhlov.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.messenger.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByNickname(String nickname);
    User findByActivationCode(String activationCode);
    List<User> findAllByAccountActivity(boolean isDeleted);
}
