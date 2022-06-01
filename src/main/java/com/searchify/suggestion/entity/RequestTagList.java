package com.searchify.suggestion.entity;

import java.util.List;

public class RequestTagList {
	
	private String domain;
	
	private List<String> tags;

	public RequestTagList(String domain, List<String> tags) {
		super();
		this.domain = domain;
		this.tags = tags;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "RequestTagList [domain=" + domain + ", tags=" + tags + "]";
	}
	
	

}
