package ru.clevertec.videohosting_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.videohosting_api.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long userId);

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);
}
