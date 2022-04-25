package com.searchify.suggestion.repositories;


import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

import com.searchify.suggestion.entity.Suggestion;

import reactor.core.publisher.Mono;

public interface SuggestionRepository extends  ReactiveNeo4jRepository<Suggestion, String> {

//	@Query("MATCH (suggestion:Suggestion) WHERE suggestion.title CONTAINS $title RETURN suggestion")
//	List<Suggestion> findSearchResults(@Param("title") String title);
	
	Mono<Suggestion> findOneByTitle(String title);
	
	Mono<Suggestion> findById(String id);
//	
}