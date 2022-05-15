package com.searchify.suggestion.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.searchify.suggestion.entity.SimpleSuggestion;
import com.searchify.suggestion.entity.Suggestion;
import com.searchify.suggestion.entity.SuggestionResultDto;
import com.searchify.suggestion.entity.Tag;
import com.searchify.suggestion.services.DeepaiService;
import com.searchify.suggestion.services.SuggestionService;
import com.searchify.suggestion.services.WordaiService;

@RestController
class SuggestionController {

	private final SuggestionService suggestionService;
	
	private final WordaiService wordaiService;
	
	private final DeepaiService deepaiService;

	SuggestionController(SuggestionService suggestionService, DeepaiService deepaiService, WordaiService wordaiService) {
		this.suggestionService = suggestionService;
		this.deepaiService = deepaiService;
		this.wordaiService = wordaiService;
	}

    @CrossOrigin
	@GetMapping("/suggestions/all")
	Collection<Suggestion> getAllSuggestions() {
		return suggestionService.getAllSuggestions();
	}
    
    @CrossOrigin
   	@GetMapping("/suggestion/labels")
   	List<String> getAllLabels() {
   		return suggestionService.getAllLabels();
   	}
    
    @CrossOrigin
   	@PostMapping("/suggestion/tags")
   	List<Tag> getAllTags(@RequestBody Map<String, String> params) {
   		return suggestionService.getAllTags(params.get("label"));
   	}
    
    @CrossOrigin
   	@PostMapping("/suggestion/tags/attachMulti")
    Suggestion attachMultiTags(@RequestBody Map<String, Object> params) {
   		return suggestionService.attachMultiTags((String)params.get("sugId"),(List<String>) params.get("tagIds"));
   	}
    
    @CrossOrigin
   	@PostMapping("/suggestion/tags/attach")
    Suggestion attachTag(@RequestBody Map<String, String> params) {
   		return suggestionService.attachSuggestion(params.get("sugId"), params.get("tagId"));
   	}

    
    @CrossOrigin
	@GetMapping("/deepai/text")
	String generateText() {
		return deepaiService.generateText();
	}
    
    
    @CrossOrigin
    @PostMapping("/wordai/text")
	List<String> generateWordAiText(@RequestParam("text") String text) {
		return wordaiService.generateText(text);
	}
    
    @CrossOrigin
	@GetMapping("/suggestion/domain")
	List<SuggestionResultDto> searchByDomain(@RequestParam("domain") String domain) {
		return new ArrayList(suggestionService.searchSuggestionByDomain(stripWildcards(domain)));
	}
    
    @CrossOrigin
	@PostMapping("/suggestion/multi")
	Collection<Suggestion> searchByMultiParams(@RequestBody List<String> tags) {
		return suggestionService.getSuggestionsByParameters(tags);
	}

    @CrossOrigin
	@PostMapping("/suggestion/update")
	Suggestion update(@RequestBody Suggestion suggestion) {
		return suggestionService.updateSuggestion(suggestion);
	}
    
    @CrossOrigin
   	@PostMapping("/suggestion/create")
   	Suggestion create(@RequestBody SimpleSuggestion simpleSuggestion) {
   		return suggestionService.createSuggestion(simpleSuggestion);
   	}

	private static String stripWildcards(String title) {
		String result = title;
		if (result.startsWith("*")) {
			result = result.substring(1);
		}
		if (result.endsWith("*")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}

