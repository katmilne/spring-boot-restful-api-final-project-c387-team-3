package com.t3.repository;

import com.t3.models.User;
import com.t3.models.UserHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(UserHistoryRepositoryTest.TestConfig.class)
class UserHistoryRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    private User testUser;
    private UserHistory testUserHistory;

    @BeforeEach
    public void setup() {
        // Initialize test data
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(testUser);

        testUserHistory = new UserHistory();
        testUserHistory.setEventId("E12345");
        testUserHistory.setEventName("Test Event");
        testUserHistory.setCity("Test Location");
        testUserHistory.setDateTime("2024-05-01T20:00:00");
        testUserHistory.setGenre("Rock");
        testUserHistory.setVenueName("Test Venue");
        testUserHistory.setUser(testUser);
        userHistoryRepository.save(testUserHistory);
    }

    @Test
    void findByUser() {
        // Retrieve the user history using the repository method
        Set<UserHistory> historySet = userHistoryRepository.findByUser(testUser);
        assertNotNull(historySet);
        assertFalse(historySet.isEmpty());
        assertTrue(historySet.stream().anyMatch(history -> history.getEventName().equals(testUserHistory.getEventName())));
    }
}
