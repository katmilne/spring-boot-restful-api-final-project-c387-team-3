package com.t3.service;

import com.t3.models.Event;
import com.t3.models.User;

import java.time.LocalDate;
import java.util.List;

public interface EventService {
    List<Event> getEventByCity(String city);
    List<Event> getEventByDate(LocalDate startDate, LocalDate endDate);
    List<Event> getEventByName(String name);
    List<Event> getUpcomingEvents(User user);
    void saveEvent(Event event);
    Event getEventByIdAndUser(long eventId, User user);
    void deleteEvent(Event eventToDelete);
}
