package com.service.mailer.repository;

import com.service.mailer.models.Emails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepo extends JpaRepository<Emails, Integer> {
    @Query("SELECT e FROM Emails e WHERE " +
            "(:name IS NULL OR e.name = :name) AND" +
            "(:keyword IS NULL OR e.htmlContent LIKE %:keyword%)")
    Page<Emails> findByParams(
            @Param("name") String name,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
