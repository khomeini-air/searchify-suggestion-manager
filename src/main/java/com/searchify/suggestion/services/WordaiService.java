package com.searchify.suggestion.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONObject;

@Service
public class WordaiService {
	
	@Value("${wordai.base.url}")
	private String baseUrl;
	
	@Value("${wordai.email}")
	private String email;
	
	@Value("${wordai.key}")
	private String key;
	
//	https://wai.wordai.com/api/rewrite?email=tu.buffalo.kid@gmail.com&key=f3fdc268f84ae49daa7a2f1cca453ec0&input=This is a test.&rewrite_num=2&uniqueness=3&return_rewrites=true

	
	public List<String> generateText(String inputText) {
		
		String requestString = baseUrl + "email=" + email
									   + "&key="  + key
									   + "&input="  + inputText
									   + "&rewrite_num=3&uniqueness=3&return_rewrites=true";
		
		RestTemplate restTemplate = new RestTemplate();
   	 	HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);
        JSONObject data = restTemplate.exchange(requestString, HttpMethod.POST, entity, JSONObject.class).getBody();
        
		return (List<String>) data.get("rewrites");
	}

}
