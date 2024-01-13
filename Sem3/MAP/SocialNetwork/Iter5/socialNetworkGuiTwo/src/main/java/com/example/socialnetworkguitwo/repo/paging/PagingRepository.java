package com.example.socialnetworkguitwo.repo.paging;

import com.example.socialnetworkguitwo.domain.Entity;
import com.example.socialnetworkguitwo.repo.Repository;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
}
