package com.mysite.sbb.question;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

	private final QuestionService questionService;
	private final UserService userService;
//	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/list")
	public String getList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
	@RequestParam(value="keyword", defaultValue="")String keyword) {
		log.info("page: {}, keyword: {}", page, keyword);
		
		Page<Question> paging = questionService.getList(page, keyword);
		model.addAttribute("paging", paging);
		model.addAttribute("keyword", keyword);
		return "question_list";
	}

	@GetMapping("/detail/{id}")
	public String getDetail(@PathVariable("id") Integer id, Model model, AnswerForm answerForm) {
		Question question = questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}

	@GetMapping("/create")
	public String getCreateQuestion(QuestionForm questionForm) {
		return "question_form";
	}

	@PostMapping("/create")
	public String postCreateQuestion(@Valid QuestionForm questionForm, BindingResult bindingResult,
			Principal principal) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}

		SiteUser user = userService.getUser(principal.getName());
		questionService.create(questionForm.getSubject(), questionForm.getContent(), user);
		return "redirect:/question/list";
	}

	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "question_form";
	}

	@PostMapping("/modify/{id}")
	public String questionModify(

			@Valid QuestionForm questionForm, BindingResult result, @PathVariable("id") Integer id,
			Principal principal) {

		if (result.hasErrors()) {
			return "question_form";
		}
		Question question = questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}
		questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%d", id);
	}

	@GetMapping("/delete/{id}")
	public String questionDelete(@PathVariable("id") Integer id, Principal principal) {

		Question question = questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}
		questionService.delete(question);
		return "redirect:/";
	}
	
	@GetMapping("/vote/{id}")
	public String questionVote(
			@PathVariable("id")Integer id,
			Principal principal
			) {
		Question question = questionService.getQuestion(id);
		SiteUser user = userService.getUser(principal.getName());
		questionService.vote(question, user);
		return String.format("redirect:/question/detail/%d", id);
	}
}
