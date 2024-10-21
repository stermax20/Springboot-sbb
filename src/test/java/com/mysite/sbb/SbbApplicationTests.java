package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;

@SpringBootTest
class SbbApplicationTests {

//	@Autowired
//	private QuestionRepository questionRepository;
//	@Autowired
//	private AnswerRepository answerRepository;
	@Autowired
	private QuestionService questionService;
	
//	@Test
//	void testJpa() {
//		Question q1 = new Question();
//		q1.setSubject("sbb가 무엇인가요?");
//		q1.setContent("sbb에 대해 알고 싶습니다.");
//		q1.setCreatedDate(LocalDateTime.now());
//		this.questionRepository.save(q1);
//
//		Question q2 = new Question();
//		q2.setSubject("2024-09-02에 취임한 GBSW의 교장선생님은?");
//		q2.setContent("이창석 교장선생님");
//		q2.setCreatedDate(LocalDateTime.now());
//		this.questionRepository.save(q2);
//	}

//	@Test
//	void testJpa() {
//		List<Question> all = this.questionRepository.findAll();
//		assertEquals(2, all.size());
//	}

//	@Test
//	void testJpa() {
//		Optional<Question> q = this.questionRepository.findById(1);
//		if (q.isPresent())
//			assertEquals("sbb가 무엇인가요?", q.get().getSubject());
//	}

//	@Test
//	void testJpa() {
//		List<Question> list = this.questionRepository.findBySubjectContainsIgnoreCase("무");
//		assertEquals(1, list.size());
//	}

//	@Test
//	void testJpa() {
//		Optional<Question> oq = questionRepository.findById(1);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		q.setSubject("수정된 제목");
//		this.questionRepository.save(q);
//	}

//	@Test
//	void testJpa() {
//		long count = this.questionRepository.count();
//
//		Optional<Question> oq = questionRepository.findById(1);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		this.questionRepository.delete(q);
//		assertEquals(count - 1, this.questionRepository.count());
//	}

//	@Test
//	void testJpa() {
//	Optional<Question> oq = questionRepository.findById(2);
//	assertTrue(oq.isPresent());
//	Question q = oq.get();
//
//	Answer a = new Answer();
//	a.setContent("스프링부트 게시판입니다.");
//	a.setQuestion(q);
//	a.setCreatedDate(LocalDateTime.now());
//	this.answerRepository.save(a);
//	}

//	@Test
//	void testJpa() {
//	Optional<Answer> oa = answerRepository.findById(1);
//	assertTrue(oa.isPresent());
//	Answer a = oa.get();
//
//	Question q = a.getQuestion();
//	assertEquals(2, q.getId());
//	}

//	@Test
//	void testJpa() {
//	Optional<Question> oq = questionRepository.findById(2);
//	assertTrue(oq.isPresent());
//	Question q = oq.get();
//
//	List<Answer> answers = q.getAnswers();
//	assertEquals(1, answers.size());
//	}

//	@Transactional
//	@Test
//	void testJpa() {
//	Optional<Question> oq = questionRepository.findById(2);
//	assertTrue(oq.isPresent());
//	Question q = oq.get();
//
//	List<Answer> answers = q.getAnswers();
//	assertEquals(1, answers.size());
//	}

	@Test
	void testJpa() {
		for (int i = 1; i < 300; i++) {
			String subject = String.format("테스트 데이터 %03d입니다.", i);
			String content = String.format("테스트 데이터 %03d의 내용입니다.", i);
			questionService.create(subject, content, null);
		}
	}
}