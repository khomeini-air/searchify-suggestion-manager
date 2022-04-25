package com.searchify.suggestion.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.internal.InternalNode;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import com.searchify.suggestion.entity.Relationship;
import com.searchify.suggestion.entity.Suggestion;
import com.searchify.suggestion.entity.SuggestionResultDto;
import com.searchify.suggestion.entity.Tag;
import com.searchify.suggestion.repositories.SuggestionRepository;

@Service
public class SuggestionService {

	private final SuggestionRepository suggestionRepository;

	private final Neo4jClient neo4jClient;

	private final Driver driver;

	private final DatabaseSelectionProvider databaseSelectionProvider;

	SuggestionService(SuggestionRepository suggestionRepository,
				 Neo4jClient neo4jClient,
				 Driver driver,
				 DatabaseSelectionProvider databaseSelectionProvider) {

		this.suggestionRepository = suggestionRepository;
		this.neo4jClient = neo4jClient;
		this.driver = driver;
		this.databaseSelectionProvider = databaseSelectionProvider;
	}


	public int voteInMovieByTitle(String title) {
		return this.neo4jClient
				.query( "MATCH (m:Movie {title: $title}) " +
						"WITH m, coalesce(m.votes, 0) AS currentVotes " +
						"SET m.votes = currentVotes + 1;" )
				.in( database() )
				.bindAll(Map.of("title", title))
				.run()
				.counters()
				.propertiesSet();
	}

//	public List<SuggestionResultDto> searchMoviesByTitle(String title) {
//		return this.suggestionRepository.findSearchResults(title)
//				.stream()
//				.map(SuggestionResultDto::new)
//				.collect(Collectors.toList());
//	}


	private String database() {
		return databaseSelectionProvider.getDatabaseSelection().getValue();
	}
	
	public Collection<SuggestionResultDto> searchSuggestionByDomain(String name) {
		return this.neo4jClient
		.query("" +
				
				"MATCH (p:Suggestion)-[r:IS_LINKED_WITH]" + 
				"->(d: Domain)" + 
				"where (d.name contains $name)" +
				"RETURN ID(p) as id, p.name as name, p.title as title, p.description as description"
		)
		.bindAll(Map.of("name", name))
		.fetchAs(SuggestionResultDto.class).mappedBy((ts, r) ->
		new SuggestionResultDto (new Suggestion((r.get("id") + ""),
				       (r.get("name").asString()), 
					   (r.get("title").asString()), 
					   (r.get("description").asString()),
					   (Object) (r.get("relationships"))
					   ))).all();
	}
	
	public Collection<Suggestion> getSuggestionsByParameters(String name) {
		String queryString = "MATCH (p:Suggestion)-[r:IS_LINKED_WITH]" + 
				"->(d: Domain)" + 
				"where (d.name contains $name)" +
				"RETURN ID(p) as id, p.name as name, p.title as title, p.description as description";
		Collection<Suggestion> suggestions =  this.neo4jClient
		.query(queryString)
		.bindAll(Map.of("name", name))
		.fetchAs(Suggestion.class).mappedBy((ts, r) ->
		new Suggestion((r.get("id") + ""),
				       (r.get("name").asString()), 
					   (r.get("title").asString()), 
					   (r.get("description").asString()), null))
		.all();
		
		return suggestions;
	}
	
	public Collection<Suggestion> getAllSuggestions() {
		 
		Collection<Suggestion> a =		this.neo4jClient
		.query("" +
				
				"MATCH (p:Suggestion)-[rel]-(r) WITH p, collect(r) as rs " + 
				"RETURN ID(p) as id, p.name as name, p.title as title, p.description as description, rs as relationships"
		)
		.fetchAs(Suggestion.class).mappedBy((ts, r) ->
		new Suggestion((r.get("id") + ""),
					   (r.get("name").asString()), 
					   (r.get("title").asString()), 
					   (r.get("description").asString()),
					   (r.get("relationships").asList((rel) -> 
						   new Relationship((((List<String>)((InternalNode) rel.asEntity() ).labels()).get(0)),
								            rel.get("name").asString())))
			
						
					   )).all();

		return a;
	}
	
	public Suggestion updateSuggestion(Suggestion suggestion) {
		String queryString = "MATCH (p:Suggestion)" + 
				" where id(p) = " + suggestion.getId() +
				" set p ={name:'" +suggestion.getName()+"', title: '" + suggestion.getTitle() +"', description: '" + suggestion.getDescription()+"'}" +
				" RETURN ID(p) as id, p.name as name, p.title as title, p.description as description";
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
		.query(queryString)
		.fetchAs(Suggestion.class).mappedBy((ts, r) ->
		new Suggestion((r.get("id") + ""),
				       (r.get("name").asString()), 
					   (r.get("title").asString()), 
					   (r.get("description").asString()),
					   (Object) (r.get("relationships"))
					   )).all();
		if(list !=null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	public List<String> getAllLabels() {
		 String queryString = "MATCH (n) RETURN distinct labels(n)";
		 
		 Object result =  this.neo4jClient
					.query(queryString)
					.fetchAs(Object.class).mappedBy((ts, r) ->
					String.valueOf(r.get(0).get(0)).replace("\"", ""))
		 			.all();
		 
		 return (List<String>) result;
	}
	
	public List<Tag> getAllTags(String label) {
		
		String queryString = "MATCH (n:" + label + ") RETURN ID(n) as id, n.name as name";
		 
		 Object result =  this.neo4jClient
					.query(queryString)
					.fetchAs(Object.class).mappedBy((ts, r) ->
					new Tag(r.get("id") + "", 
						    r.get("name").asString(),
						    r.get("name").asString()))
		 			.all();
		 
		 return (List<Tag>) result;
		
	}
	
	public Suggestion attachSuggestion(String sugId, String tagId) {
		String queryString = " MATCH (s) where id(s) = " + sugId + 
				             " MATCH (n) where id(n) = " + tagId +
				             " CREATE (s)-[rel:IS_LINKED_WITH]->(n)" +
				             " RETURN ID(s) as id, s.name as name, s.title as title, s.description as description";
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
		.query(queryString)
		.fetchAs(Suggestion.class).mappedBy((ts, r) ->
		new Suggestion((r.get("id") + ""),
				       (r.get("name").asString()), 
					   (r.get("title").asString()), 
					   (r.get("description").asString()),
					   (Object) (r.get("relationships"))
					   )).all();
		if(list !=null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
}