package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

	public final QuestionRepository questionRepository;

	public Page<Question> getList(int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createdDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return questionRepository.findAll(pageable);
	}

	public Question getQuestion(Integer id) {
		Optional<Question> question = questionRepository.findById(id);
		if (question.isPresent()) {
			return question.get();
		} else {
			throw new DataNotFoundException("Question not found.");
		}
	}

	public void create(String subject, String content, SiteUser author) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setAuthor(author);
		q.setCreatedDate(LocalDateTime.now());
		questionRepository.save(q);
	}
}
