package com.service.answers.answerservice.repositories;

import com.service.answers.answerservice.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question, String> {
    @Query("SELECT q FROM Question q WHERE" +
            "(:question IS NULL OR q.question LIKE %:question%) AND " +
            "(:status IS NULL OR q.status =:status ) AND " +
            "(:userId IS NULL OR q.user.id =:userId) ")

    Page<Question> findByParams(
            @Param("question") String question,
            @Param("userId") Integer userId,
            Pageable pageable
    );
}
