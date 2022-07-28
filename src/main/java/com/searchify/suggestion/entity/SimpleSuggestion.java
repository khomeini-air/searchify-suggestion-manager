package com.searchify.suggestion.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

public class SimpleSuggestion {

	@Id
	private final String name;

	private String title;
	
	private String keywords;

	private String description;
	
	private String domain;
	
	private Object relationships;

	public SimpleSuggestion(String name, String title, String keywords, String description, String domain, Object relationships) {
		this.name = name;
		this.title = title;
		this.keywords = keywords;
		this.description = description;
		this.domain = domain;
		this.relationships = relationships;
	}
	
	public String getName() {
		return name;
	}
	

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getKeywords() {
		return keywords;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public Object getRelationships() {
		return relationships;
	}

	public void setRelationships(Object relationships) {
		this.relationships = relationships;
	}

	

	
	
}