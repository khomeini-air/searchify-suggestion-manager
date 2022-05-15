package com.searchify.suggestion.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

public class SimpleSuggestion {

	@Id
	private final String name;

	private String title;

	private String description;
	
	private Object relationships;

	public SimpleSuggestion(String name, String title, String description, Object relationships) {
		this.name = name;
		this.title = title;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getRelationships() {
		return relationships;
	}

	public void setRelationships(Object relationships) {
		this.relationships = relationships;
	}

	
	
}