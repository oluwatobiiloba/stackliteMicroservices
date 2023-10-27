package com.service.question.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.service.question.model.User;

@Repository
public interface UsersRepo extends JpaRepository<User, Integer> {

}
