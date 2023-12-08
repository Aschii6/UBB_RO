package com.example.sem7.repository;

import com.example.sem7.domain.Movie;
import com.example.sem7.dto.MovieFilterDTO;
import com.example.sem7.repository.paging.Page;
import com.example.sem7.repository.paging.Pageable;
import com.example.sem7.repository.paging.PagingRepository;

import java.util.List;

public interface MovieRepository extends PagingRepository<Long, Movie> {
    List<Integer> getYears();

    Page<Movie> findAllOnPage(Pageable pageable, MovieFilterDTO filter);
}
