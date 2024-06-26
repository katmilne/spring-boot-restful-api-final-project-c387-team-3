package com.t3.repository;

import com.t3.models.Event;
import com.t3.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FavoriteEventRepository extends JpaRepository<Event, Long> {
    Set<Event> findByUser(User user);
    Event findByIdAndUser(Long eventId, User user);
}
