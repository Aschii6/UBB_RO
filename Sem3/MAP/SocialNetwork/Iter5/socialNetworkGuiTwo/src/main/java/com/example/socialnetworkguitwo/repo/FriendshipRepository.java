package com.example.socialnetworkguitwo.repo;

import com.example.socialnetworkguitwo.domain.Friendship;
import com.example.socialnetworkguitwo.domain.Status;
import com.example.socialnetworkguitwo.domain.Tuple;
import com.example.socialnetworkguitwo.repo.paging.Page;
import com.example.socialnetworkguitwo.repo.paging.Pageable;
import com.example.socialnetworkguitwo.repo.paging.PagingRepository;

public interface FriendshipRepository extends PagingRepository<Tuple<Long, Long>, Friendship> {
    Iterable<Friendship> getAllFriendReqs(Long userId);
    Iterable<Friendship> getAllFriendships(Long userId);
    Page<Friendship> getPagedFriendshipsOfAUser(Pageable pageable, Long userId, Status status);
}
