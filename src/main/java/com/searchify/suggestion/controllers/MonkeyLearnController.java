package com.searchify.suggestion.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.searchify.suggestion.services.MonkeyLearnService;

import net.minidev.json.JSONObject;

@RestController
class MonkeyLearnController {

	private final MonkeyLearnService monkeyLearnService;

	MonkeyLearnController(MonkeyLearnService monkeyLearnService) {
		this.monkeyLearnService = monkeyLearnService;
	}
	
	@CrossOrigin
	@PostMapping("/tagging/tags")
	List<JSONObject> attachTag(@RequestBody List<String> data) {
	  return monkeyLearnService.getClassifications(data);
	}

}

