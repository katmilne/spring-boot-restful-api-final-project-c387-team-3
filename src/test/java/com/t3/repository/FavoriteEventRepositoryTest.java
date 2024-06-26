package com.t3.repository;

import com.t3.models.Event;
import com.t3.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(FavoriteEventRepositoryTest.TestConfig.class)
class FavoriteEventRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FavoriteEventRepository favoriteEventRepository;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    public void setup() {
        // Initialize our test data
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        testEvent = new Event();
        testEvent.setEventId("E12345");
        testEvent.setEventName("Test Event");
        testEvent.setCity("Test Location");
        testEvent.setDateTime("2024-05-01T20:00:00");
        testEvent.setGenre("Rock");
        testEvent.setVenueName("Test Venue");
        testEvent.setUser(testUser);
        favoriteEventRepository.save(testEvent);
    }

    @Test
    void findByIdAndUser() {
        // Retrieve the event using the repository method
        Event foundEvent = favoriteEventRepository.findByIdAndUser(testEvent.getId(), testUser);
        assertNotNull(foundEvent);
        assertEquals(testEvent.getEventName(), foundEvent.getEventName());
        assertEquals(testUser.getUsername(), foundEvent.getUser().getUsername());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
