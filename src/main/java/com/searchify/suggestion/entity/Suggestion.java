package com.searchify.suggestion.entity;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Suggestion")
public class Suggestion {

	@Id
	private final String name;
	
	private String id;

	private String title;

	private String description;
	
	private Object relationships;

	public Suggestion(String id, String name, String title, String description, Object relationships) {
		this.id = id;
		this.name = name;
		this.title = title;
		this.description = description;
		this.relationships = relationships;
	}
	
	public String getName() {
		return name;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
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