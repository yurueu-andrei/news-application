package ru.clevertec.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.security.entity.User;

import java.util.Optional;

/**
 * JPA repository for Users.
 *
 * @author Yuryeu Andrei
 * @see JpaRepository
 * @see User
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
