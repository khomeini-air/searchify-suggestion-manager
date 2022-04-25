package com.searchify.suggestion.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.searchify.suggestion.entity.Movie;

public interface MovieRepository extends Repository<Movie, String> {

	@Query("MATCH (movie:Movie) WHERE movie.title CONTAINS $title RETURN movie")
	List<Movie> findSearchResults(@Param("title") String title);
}