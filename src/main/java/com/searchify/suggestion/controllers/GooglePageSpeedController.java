package com.searchify.suggestion.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.searchify.suggestion.services.PageSpeedService;

import net.minidev.json.JSONObject;

@RestController
class GooglePageSpeedController {

	private final PageSpeedService pageSpeedService;

	GooglePageSpeedController(PageSpeedService pageSpeedService) {
		this.pageSpeedService = pageSpeedService;
	}
	
	@CrossOrigin
	@PostMapping("/pagespeed/checkspeed")
	java.util.LinkedHashMap check(@RequestBody Map<String, String> data) {
	  return pageSpeedService.getPageSpeed(data.get("url"), null, data.get("category"), null, data.get("strategy"), null, null);
	}

}

