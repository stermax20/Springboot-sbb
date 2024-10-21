package com.mysite.sbb.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
	List<Question> findBySubject(String subject);
	List<Question> findBySubjectContainsIgnoreCase(String subject);
	List<Question> findBySubjectLike(String subject);
	Page<Question> findAll(Pageable pageable);
}