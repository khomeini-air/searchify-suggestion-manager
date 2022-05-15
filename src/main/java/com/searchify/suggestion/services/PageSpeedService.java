package com.searchify.suggestion.services;

import java.util.Arrays;
import java.util.LinkedHashMap;
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
public class PageSpeedService {
	
	@Value("${pagespeed.base.url}")
	private String baseUrl;
	
	@Value("${pagespeed.apiKey}")
	private String apiKey;
	
	
	public java.util.LinkedHashMap getPageSpeed(String url, String captchaToken,  String category,
			                       String locale, String strategy, String utm_campaign, String utm_source) {
		
		String requestString = baseUrl + "?key="  + apiKey
									   + "&url="  + url
									   + "&category="  + category
									   + "&strategy="  + strategy;
		
		RestTemplate restTemplate = new RestTemplate();
   	 	HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);
        JSONObject data = restTemplate.exchange(requestString, HttpMethod.GET, entity, JSONObject.class).getBody();
        java.util.LinkedHashMap lighthouseResultJsonObject = (LinkedHashMap) data.get("lighthouseResult");
        java.util.LinkedHashMap categories = (LinkedHashMap) lighthouseResultJsonObject.get("categories");
        java.util.LinkedHashMap seo = (LinkedHashMap) categories.get("seo");
		return seo;
	}

}
