package com.service.answers.answerservice.repositories;

import com.service.answers.answerservice.model.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerRepo extends JpaRepository<Answer,String> {
    @Query("SELECT a FROM Answer a where" +
            ":answer IS NULL OR a.answer Like %:answer% AND" +
            ":userId IS NULL OR a.user.id =:userId"
    )
    Page<Answer> findByParams(
            @Param("answer") String answer,
            @Param("userId") Integer userId,
            Pageable pageable
    );
}
