package com.user.service.repositories;

import com.user.service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:phoneNumber IS NULL OR u.phoneNumber =:phoneNumber ) AND " +
            "(:lastName IS NULL OR u.lastName =:lastName ) AND " +
            "(:firstName IS NULL OR u.firstName =:firstName ) AND " +
            "(:stack IS NULL OR u.stack =:stack ) AND " +
            "(:email IS NULL OR u.email =:email ) AND " +
            "(:id IS NULL OR u.id =:id) ")
    Page<User> findByParams(
            @Param("username") String username,
            @Param("phoneNumber") Integer phoneNumber,
            @Param("id") Integer id,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("stack") String stack,
            @Param("email") String email,
            Pageable pageable);

}
