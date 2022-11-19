package com.cf.cabinescaperoom.repositories;

import com.cf.cabinescaperoom.models.Provider;
import com.cf.cabinescaperoom.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(@Param("username") String username);

    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.provider = ?2 WHERE u.username = ?1")
    void updateProvider(String username, Provider provider);

    @Query("SELECT u from User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    User findByResetPasswordToken(String token);
}
