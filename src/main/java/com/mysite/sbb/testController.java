package com.mysite.sbb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class testController {
	
	@GetMapping("/{path}")
	public String testPath(@PathVariable("path") String path) {
		return path;
	}
	
	@GetMapping()
	public String testQuery(
			@RequestParam(name="page", required = false) String page,
			@RequestParam(name="order", required = false) String order) {
		if(page == null) page = "1";
		if(order == null) order = "desc";
		return "page="+page+" order="+order;
	}
}
