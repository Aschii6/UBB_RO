package com.example.sem7.repository.paging;

import com.example.sem7.domain.Entity;
import com.example.sem7.repository.Repository;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}
