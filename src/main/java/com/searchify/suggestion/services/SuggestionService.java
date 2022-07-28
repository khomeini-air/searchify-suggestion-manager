package com.searchify.suggestion.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.neo4j.cypherdsl.core.Relationship.Details;
import org.neo4j.driver.Driver;
import org.neo4j.driver.internal.InternalNode;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import com.searchify.suggestion.entity.Domain;
import com.searchify.suggestion.entity.Relationship;
import com.searchify.suggestion.entity.RequestTagList;
import com.searchify.suggestion.entity.SimpleSuggestion;
import com.searchify.suggestion.entity.Suggestion;
import com.searchify.suggestion.entity.SuggestionResultDto;
import com.searchify.suggestion.entity.Tag;
import com.searchify.suggestion.repositories.SuggestionRepository;

import net.minidev.json.JSONObject;

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

	private String database() {
		return databaseSelectionProvider.getDatabaseSelection().getValue();
	}

	public Collection<Suggestion> searchSuggestionByDomain(String name) {
		
//		Collection<SuggestionResultDto> a =  this.neo4jClient
//				.query("" +
//
//				"MATCH (p:Suggestion)-[r:IS_LINKED_WITH]" + 
//				"->(d: Domain)" + 
//				"where (d.name contains $name)" +
//				"RETURN ID(p) as id, p.name as name, p.title as title, p.description as description"
//						)
//				.bindAll(Map.of("name", name))
//				.fetchAs(SuggestionResultDto.class).mappedBy((ts, r) ->
//				new SuggestionResultDto (new Suggestion((r.get("id") + ""),
//						(r.get("name").asString()), 
//						(r.get("title").asString()), 
//						(r.get("description").asString()),
//						(Object) (r.get("relationships"))
//						))).all();
		
		Collection<Suggestion> a =  this.neo4jClient
				.query("" +
				" MATCH (p:Suggestion)-[rel]-(r:Tag) WITH p, collect(r) as rs" +
				" MATCH (p:Suggestion)-[r:IS_LINKED_WITH]" + 
				"->(d: Domain)" + 
				"where (d.name contains $name)" +
				"RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships"
						)
				.bindAll(Map.of("name", name))
				.fetchAs(Suggestion.class).mappedBy((ts, r) -> 
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(r.get("relationships").asList((rel) -> 
						new Relationship((( (List<String>)((InternalNode) rel.asEntity() ).labels())!=null ? rel.get("name").asString():null),
								Long.toString(((InternalNode) rel.asEntity() ).id()))))
				)).all();
		return a;
	}

	public Collection<Suggestion> getSuggestionsByParameters(RequestTagList request) {
		String domain = request.getDomain();
		List<String> tags = request.getTags();
		Map<String, Object> tagging = new HashMap<String, Object>();
		String match = "MATCH (p:Suggestion)-[rel]-(r:Tag) WITH p, collect(r) as rs";
		String where = "WHERE";
		String return_string = "RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships";
		int count = 0;
		for (String tagName : tags) {
			match =  match + " MATCH (p:Suggestion)-[r" + count +":IS_LINKED_WITH]" + "->(t"+count+": Tag)";
			if(count==0) {
				where = where +  " (t"+ count+".name contains $tag" + count + ")";
			}else {
				where = where +  " AND (t"+ count+".name contains $tag" + count + ")";
			}
			tagging.put("tag"+count, tagName);
			count ++;
		}
		String queryString = match + where + return_string;
		Collection<Suggestion> suggestions =  this.neo4jClient
				.query(queryString)
				.bindAll(tagging)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()), 
						(r.get("relationships").asList((rel) -> 
						new Relationship((( (List<String>)((InternalNode) rel.asEntity() ).labels())!=null ? ((List<String>)((InternalNode) rel.asEntity() ).labels()).get(0):null),
								rel.get("name").asString())))
				)).all();
		
		if(suggestions == null || suggestions.isEmpty()) {
			suggestions = searchSuggestionByDomain(domain);
		}

		return suggestions;
	}

	public Collection<Suggestion> getAllSuggestions() {

		Collection<Suggestion> suggestions =		this.neo4jClient
				.query("" +

				"MATCH (p:Suggestion)-[rel]-(r:Tag) WITH p, collect(r) as rs " + 
				"RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships"
						)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(r.get("relationships").asList((rel) -> 
						new Relationship((((List<String>)((InternalNode) rel.asEntity() ).labels()).get(0)),
								rel.get("name").asString())))
						)).all();

		return suggestions;
	}

	public Suggestion createSimpleSuggestion(SimpleSuggestion simpleSuggestion) {
		String match = "MATCH (t1:Domain) Where t1.name='" + simpleSuggestion.getDomain() + "'";
		String create = " CREATE (s:Suggestion {name:'" + simpleSuggestion.getName()+ "', title: '" + simpleSuggestion.getTitle() + "', keywords: '" + simpleSuggestion.getKeywords() + "', description: '" + simpleSuggestion.getDescription()+ "'})" + " CREATE (s)-[r:IS_LINKED_WITH]->(t1)";
		String return_string = " RETURN ID(s) as id, s.name as name, s.title as title, s.keywords as keywords, s.description as description, s.relationship as relationship";;
		int count = 0;
		if (simpleSuggestion.getRelationships() != null) {
			List<Map<String, String>> relationships = (List<Map<String, String>>) simpleSuggestion.getRelationships();
		
			for(Map<String, String> relationship : relationships) {
				match = match + " MATCH (n" + count + ") where id(n" + count + ") = " + relationship.get("value");
				create = create + " CREATE (s)-[rel"+ count + ":IS_LINKED_WITH]->(n" + count + ")";
				count ++;
			}
		}

		String queryString = match + create + return_string;
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
				.query(queryString)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(Object) (r.get("relationship"))
						)).all();
		if(list !=null && !list.isEmpty()) {
			// Get the created suggestion
			Suggestion s = list.get(0);
			Collection<Suggestion> a =		this.neo4jClient
					.query("" +
							"MATCH (p:Suggestion)-[rel]-(r:Tag) WITH p, collect(r) as rs where ID(p)=" + s.getId() +  
							" RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships"
							)
					.fetchAs(Suggestion.class).mappedBy((ts, r) ->
					new Suggestion((r.get("id") + ""),
							(r.get("name").asString()), 
							(r.get("title").asString()), 
							(r.get("keywords").asString()), 
							(r.get("description").asString()),
							(r.get("relationships").asList((rel) -> 
							new Relationship((( (List<String>)((InternalNode) rel.asEntity() ).labels())!=null ? rel.get("name").asString():null),
									Long.toString(((InternalNode) rel.asEntity() ).id()))))
					)).all();
			System.out.println(a);
			return (new ArrayList<Suggestion>(a)).get(0);
		}
		return null;
	}

	public Suggestion updateSuggestion(Suggestion suggestion) {
		List<String> newTagIds = new ArrayList<String>();
		List<String> oldTagIds = new ArrayList<String>();
		if(suggestion.getRelationships() != null ) {
			List<Map> relationships = (List<Map>)suggestion.getRelationships();
			
			for(Map rel : relationships) {
				newTagIds.add(rel.get("value").toString());
			}
						
		}
		String queryString = "MATCH (p:Suggestion)" + 
				" where id(p) = " + suggestion.getId() +
				" set p ={name:'" +suggestion.getName()+"', title: '" + suggestion.getTitle() +"', keywords: '" + suggestion.getKeywords() +"', description: '" + suggestion.getDescription()+"'}" +
				" RETURN ID(p) as id, p.name as name, p.title as title, p.description as description";
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
				.query(queryString)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(Object) (r.get("relationships"))
						)).all();
		if(list !=null && !list.isEmpty()) {
			// Get the created suggestion
			Suggestion s = list.get(0);
			Collection<Suggestion> a =		this.neo4jClient
					.query("" +
							"MATCH (p:Suggestion)-[rel]-(r:Tag) WITH p, collect(r) as rs where ID(p)=" + s.getId() +  
							" RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships"
							)
					.fetchAs(Suggestion.class).mappedBy((ts, r) ->
					new Suggestion((r.get("id") + ""),
							(r.get("name").asString()), 
							(r.get("title").asString()), 
							(r.get("keywords").asString()), 
							(r.get("description").asString()),
							(r.get("relationships").asList((rel) -> 
							new Relationship((( (List<String>)((InternalNode) rel.asEntity() ).labels())!=null ?rel.get("name").asString():null),
									Long.toString(((InternalNode) rel.asEntity() ).id()))))

							)).all();
			
			Suggestion currentSuggestion = (new ArrayList<Suggestion>(a)).get(0);
			if(currentSuggestion.getRelationships() != null ) {
				currentSuggestion = dettachAllTagsFromSuggestion(currentSuggestion.getId());
			}
			
			if(!newTagIds.isEmpty()) {
				currentSuggestion = attachMultiTagsIntoSuggestion(currentSuggestion.getId(), newTagIds);
			}
			
			return currentSuggestion;
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

	public List<Tag> getAllTags(String domain) {

		String queryString = " MATCH (n:Tag)-[r:IS_LINKED_WITH]->(d: Domain) " +
				           	 " WHERE (d.name contains '"+domain +"')" +
				             " RETURN ID(n) as id, n.name as name";

		Object result =  this.neo4jClient
				.query(queryString)
				.fetchAs(Object.class).mappedBy((ts, r) ->
				new Tag(r.get("id") + "", 
						r.get("name").asString(),
						r.get("name").asString()))
				.all();

		return (List<Tag>) result;

	}

	public Suggestion attachTagIntoSuggestion(String sugId, String tagId) {
		String queryString = " MATCH (s) where id(s) = " + sugId + 
				" MATCH (n) where id(n) = " + tagId +
				" CREATE (s)-[rel:IS_LINKED_WITH]->(n)" +
				" RETURN ID(s) as id, s.name as name, s.title as title, s.keywords as keywords, s.description as description";
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
				.query(queryString)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(Object) (r.get("relationships"))
						)).all();
		if(list !=null && !list.isEmpty()) {
			// Get the created suggestion
			Suggestion s = list.get(0);
			Collection<Suggestion> a =		this.neo4jClient
					.query("" +
							"MATCH (p:Suggestion)-[rel]-(r) WITH p, collect(r) as rs where ID(p)=" + s.getId() +  
							" RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships"
							)
					.fetchAs(Suggestion.class).mappedBy((ts, r) ->
					new Suggestion((r.get("id") + ""),
							(r.get("name").asString()), 
							(r.get("title").asString()), 
							(r.get("keywords").asString()), 
							(r.get("description").asString()),
							(r.get("relationships").asList((rel) -> 
							new Relationship((( (List<String>)((InternalNode) rel.asEntity() ).labels())!=null ? rel.get("name").asString():null),
									Long.toString(((InternalNode) rel.asEntity() ).id()))))

							)).all();
			System.out.println(a);
			return (new ArrayList<Suggestion>(a)).get(0);
		}
		return null;
	}
	
	public Suggestion dettachTagFromSuggestion(String sugId, String tagId) {
		String queryString = " MATCH (s) where id(s) = " + sugId + 
				" MATCH (n) where id(n) = " + tagId +
				" DELETE (s)-[rel:IS_LINKED_WITH]->(n)" +
				" RETURN ID(s) as id, s.name as name, s.title as title, s.keywords as keywords, s.description as description";
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
				.query(queryString)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(Object) (r.get("relationships"))
						)).all();
		if(list !=null && !list.isEmpty()) {
			// Get the created suggestion
			Suggestion s = list.get(0);
			Collection<Suggestion> a =		this.neo4jClient
					.query("" +
							"MATCH (p:Suggestion)-[rel]-(r) WITH p, collect(r) as rs where ID(p)=" + s.getId() +  
							" RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships"
							)
					.fetchAs(Suggestion.class).mappedBy((ts, r) ->
					new Suggestion((r.get("id") + ""),
							(r.get("name").asString()), 
							(r.get("title").asString()), 
							(r.get("keywords").asString()), 
							(r.get("description").asString()),
							(r.get("relationships").asList((rel) -> 
							new Relationship((( (List<String>)((InternalNode) rel.asEntity() ).labels())!=null ? rel.get("name").asString():null),
									Long.toString(((InternalNode) rel.asEntity() ).id()))))

							)).all();
			System.out.println(a);
			return (new ArrayList<Suggestion>(a)).get(0);
		}
		return null;
	}

	public Suggestion attachMultiTagsIntoSuggestion(String sugId, List<String> tagIds) {

		String match = "MATCH (s) where id(s) = " + sugId;
		String create = "";
		String return_string = "";
		int count = 0;

		for(String tagId : tagIds) {
			match = match + " MATCH (n" + count + ") where id(n" + count + ") = " + tagId;
			create = create + " CREATE (s)-[rel" + count + ":IS_LINKED_WITH]->(n" + count + ")";
			return_string = " RETURN ID(s) as id, s.name as name, s.title as title, s.keywords as keywords, s.description as description";
			count ++;
		}

		String queryString = match + create + return_string;

		//		String queryString = " MATCH (s) where id(s) = " + sugId + 
		//	             " MATCH (n) where id(n) = " + tagId +
		//	             " CREATE (s)-[rel:IS_LINKED_WITH]->(n)" +
		//	             " RETURN ID(s) as id, s.name as name, s.title as title, s.description as description";
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
				.query(queryString)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(Object) (r.get("relationships"))
						)).all();
		if(list !=null && !list.isEmpty()) {
			// Get the created suggestion
			Suggestion s = list.get(0);
			Collection<Suggestion> a =		this.neo4jClient
					.query("" +
							"MATCH (p:Suggestion)-[rel]-(r) WITH p, collect(r) as rs where ID(p)=" + s.getId() +  
							" RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships"
							)
					.fetchAs(Suggestion.class).mappedBy((ts, r) ->
					new Suggestion((r.get("id") + ""),
							(r.get("name").asString()), 
							(r.get("title").asString()), 
							(r.get("keywords").asString()), 
							(r.get("description").asString()),
							(r.get("relationships").asList((rel) -> 
							new Relationship((( (List<String>)((InternalNode) rel.asEntity() ).labels())!=null ? rel.get("name").asString():null),
									Long.toString(((InternalNode) rel.asEntity() ).id()))))

							)).all();
			System.out.println(a);
			return (new ArrayList<Suggestion>(a)).get(0);
		}

		return null;
	}
	
	public Suggestion dettachMultiTagsFromSuggestion(String sugId, List<String> tagIds) {

		String match = "MATCH (s) where id(s) = " + sugId;
		String create = "";
		String return_string = "";
		int count = 0;

		for(String tagId : tagIds) {
			match = match + " MATCH (n" + count + ") where id(n" + count + ") = " + tagId;
			create = create + " DELETE (s)-[rel:IS_LINKED_WITH]->(n" + count + ")";
			return_string = " RETURN ID(s) as id, s.name as name, s.title as title, s.keywords as keywords, s.description as description";
			count ++;
		}

		String queryString = match + create + return_string;

		//		String queryString = " MATCH (s) where id(s) = " + sugId + 
		//	             " MATCH (n) where id(n) = " + tagId +
		//	             " CREATE (s)-[rel:IS_LINKED_WITH]->(n)" +
		//	             " RETURN ID(s) as id, s.name as name, s.title as title, s.description as description";
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
				.query(queryString)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(Object) (r.get("relationships"))
						)).all();
		if(list !=null && !list.isEmpty()) {
			// Get the created suggestion
			Suggestion s = list.get(0);
			Collection<Suggestion> a =		this.neo4jClient
					.query("" +
							"MATCH (p:Suggestion)-[rel]-(r) WITH p, collect(r) as rs where ID(p)=" + s.getId() +  
							" RETURN ID(p) as id, p.name as name, p.title as title, p.keywords as keywords, p.description as description, rs as relationships"
							)
					.fetchAs(Suggestion.class).mappedBy((ts, r) ->
					new Suggestion((r.get("id") + ""),
							(r.get("name").asString()), 
							(r.get("title").asString()), 
							(r.get("keywords").asString()), 
							(r.get("description").asString()),
							(r.get("relationships").asList((rel) -> 
							new Relationship((( (List<String>)((InternalNode) rel.asEntity() ).labels())!=null ? rel.get("name").asString():null),
									Long.toString(((InternalNode) rel.asEntity() ).id()))))

							)).all();
			System.out.println(a);
			return (new ArrayList<Suggestion>(a)).get(0);
		}

		return null;
	}
	
	public Suggestion dettachAllTagsFromSuggestion(String sugId) {

		String match = "MATCH (s:Suggestion) - [rel:IS_LINKED_WITH]->(t:Tag) where id(s) = " + sugId;
		String delete = " DELETE rel";
		String return_string = " RETURN ID(s) as id, s.name as name, s.title as title, s.keywords as keywords, s.description as description";

		String queryString = match + delete + return_string;

		//		String queryString = " MATCH (s) where id(s) = " + sugId + 
		//	             " MATCH (n) where id(n) = " + tagId +
		//	             " CREATE (s)-[rel:IS_LINKED_WITH]->(n)" +
		//	             " RETURN ID(s) as id, s.name as name, s.title as title, s.description as description";
		List<Suggestion> list = (List<Suggestion>) this.neo4jClient
				.query(queryString)
				.fetchAs(Suggestion.class).mappedBy((ts, r) ->
				new Suggestion((r.get("id") + ""),
						(r.get("name").asString()), 
						(r.get("title").asString()), 
						(r.get("keywords").asString()), 
						(r.get("description").asString()),
						(Object) (r.get("relationships"))
						)).all();
		if(list !=null && !list.isEmpty()) {
			// Get the created suggestion
			Suggestion s = list.get(0);
			return list.get(0);
		}

		return null;
	}


	public List<Domain> getAllDomains() {

		String queryString = " MATCH (d: Domain) " +
				             " RETURN ID(d) as id, d.name as name";

		Object result =  this.neo4jClient
				.query(queryString)
				.fetchAs(Object.class).mappedBy((ts, r) ->
				new Domain(r.get("id") + "", 
						r.get("name").asString(),
						r.get("name").asString()))
				.all();

		return (List<Domain>) result;

	}
	
	public Domain createDomain(String domainName) {
		
		return null;
		
	}
	
	public JSONObject createTag(String domainName, String tagName) {
		
		JSONObject returnItem = new JSONObject();
		if(checkExistingTags(getAllTags(domainName), tagName)) {
			returnItem.put("message", "This tag is already existed!");
			returnItem.put("code", "2");
			
			return returnItem;
		}
		
		String match = "MATCH (d:Domain) Where d.name='" + domainName + "'";
		String create = " CREATE (t:Tag {name:'" + tagName + "'})" + " CREATE (t)-[r:IS_LINKED_WITH]->(d)";
		String return_string = " RETURN ID(t) as id, t.name as name";
		
		String queryString = match + create + return_string;
		
		List<Tag> list =  (List<Tag>) this.neo4jClient
				.query(queryString)
				.fetchAs(Tag.class).mappedBy((ts, r) ->
				new Tag(r.get("id") + "", 
						r.get("name").asString(),
						r.get("name").asString()))
				.all();
		if(list !=null && !list.isEmpty()) {
			// Get the created suggestion
			Tag tag = list.get(0);
			returnItem.put("message", "A new task is created successfully");
			returnItem.put("code", "1");
			returnItem.put("tag", tag);
			return returnItem;
		}
		
		returnItem.put("message", "Error");
		returnItem.put("code", "0");
		return returnItem;
	}
	
	public Tag findATag(String domainName, String tagName) {
		
		return null;
	}
	
	private Boolean checkExistingTags(List<Tag> tags, String tagName) {
		if(tags != null && tagName != null) {
			for(Tag tag : tags) {
				if(StringUtils.upperCase(tag.getName()).equals(StringUtils.upperCase(tagName))) {
					return true;
				}
			}
		}
		return false;
	}

}