package com.mysite.sbb.answer;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;

	@PostMapping("/create/{id}")
	public String createAnswer(

			@PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Model model, Principal principal) {
		Question q = questionService.getQuestion(id);
		if (bindingResult.hasErrors()) {
			model.addAttribute(q);
			return "question_detail";
		}
		SiteUser user = userService.getUser(principal.getName());
		answerService.create(q, answerForm.getContent(), user);
		return "redirect:/question/detail/%d".formatted(id);
	}
		
	@GetMapping("/modify/{id}")
	public String answerModify(
			@PathVariable("id") Integer id,
			@RequestParam("content") String content,
			Principal principal
			) 
	{
		Answer answer = answerService.getAnswerById(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}
		answerService.modify(answer, content);
		return String.format("redirect:/question/detail/%d", answer.getQuestion().getId());
	}
	
	@GetMapping("/delete/{id}")
	public String answerDelete(@PathVariable("id") Integer id, Principal principal) {

		Answer answer = answerService.getAnswerById(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}
		answerService.delete(answer);
		return String.format("redirect:/question/detail/%d", answer.getQuestion().getId());
	}	
	
	@GetMapping("/vote/{id}")
	public String answerVote(
	  @PathVariable("id") Integer id,
	  Principal principal
	  ) {
	Answer answer = answerService.getAnswerById(id);
	SiteUser user = userService.getUser(principal.getName());
	answerService.vote(answer, user);

	return String.format("redirect:/question/detail/%d", answer.getQuestion().getId());
	}
}