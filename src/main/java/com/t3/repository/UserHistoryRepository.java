package com.t3.repository;

import com.t3.models.User;
import com.t3.models.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    Set<UserHistory> findByUser(User user);
}
