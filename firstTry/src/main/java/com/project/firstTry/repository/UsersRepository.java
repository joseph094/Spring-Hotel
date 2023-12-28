package com.project.firstTry.repository;

import com.project.firstTry.model.Roles;
import com.project.firstTry.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);

    Users findByRole(Roles roles);

    @Modifying
    @Query("DELETE FROM Users u WHERE u.id_user = :userId")
    void deleteUserById(@Param("userId") long userId);
}
