package com.searchify.suggestion.services;

import java.util.Arrays;
import java.util.HashMap;
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
public class MonkeyLearnService {
	
	@Value("${monkey.url.classifier}")
	private  String url_classifier;
	
	@Value("${monkey.model.id}")
	private  String modelId;
	
	@Value("${monkey.apiKey}")
	private String apiKey ;
	
	public List<JSONObject> getClassifications(List<String> data) {
		
		 Map<String, Object> json = new HashMap<String, Object>();
		 json.put("data", data);
		 RestTemplate restTemplate = new RestTemplate();
    	 HttpHeaders headers = new HttpHeaders();
         headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         headers.set("Authorization", "Token " + apiKey);
         headers.setContentType(MediaType.APPLICATION_JSON);
         HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>( new JSONObject(json), headers);
         List<JSONObject> result = restTemplate.exchange(url_classifier + modelId + "/classify/", HttpMethod.POST, entity, List.class).getBody();
        		
		return result;
	}

}
