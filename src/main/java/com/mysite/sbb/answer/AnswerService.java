package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	public void create(Question question, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setQuestion(question);
		answer.setContent(content);
		answer.setAuthor(author);
		answer.setCreatedDate(LocalDateTime.now());
		answerRepository.save(answer);
	}
	
	public Answer getAnswerById(Integer id) {
		return answerRepository.findById(id)
				.orElseThrow(()-> new DataNotFoundException("데이터가 없습니다."));
	}
	
	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		answerRepository.save(answer);
	}
	
	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}
	
	public void vote(Answer answer, SiteUser user) {
		answer.getVoter().add(user);
		answerRepository.save(answer);
	}
}
