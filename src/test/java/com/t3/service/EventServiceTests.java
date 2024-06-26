package com.t3.service;

import com.t3.models.Event;
import com.t3.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTests {

    private EventServiceImpl eventService;

    public EventServiceTests(){
        FavoriteEventRepositoryStub stub = new FavoriteEventRepositoryStub();
        eventService = new EventServiceImpl(stub);
    }

    @Test
    public void findByIdAndUserTest(){
       User user = new User();
       Event event = eventService.getEventByIdAndUser(15,user);
       assertNotNull(event);
       assertTrue(event instanceof Event);
    }
}
