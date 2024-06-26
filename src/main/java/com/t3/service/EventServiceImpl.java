package com.t3.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3.models.Event;
import com.t3.models.User;
import com.t3.models.UserHistory;
import com.t3.repository.FavoriteEventRepository;
import com.t3.repository.UserHistoryRepository;
import com.t3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    FavoriteEventRepository favoriteEventRepository;
    @Autowired
    UserHistoryRepository userHistoryRepo;

    public EventServiceImpl(FavoriteEventRepository favoriteEventRepository) {
        this.favoriteEventRepository = favoriteEventRepository;
    }

    @Value("${ticketmaster.api.key}")
    private String apiKey;
    private final String apiUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=";
    @Override
    public List<Event> getEventByCity(String city) {
        return List.of();
    }

    @Override
    public List<Event> getEventByDate(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<Event> getEventByName(String name) {
        return List.of();
    }

    @Override
    public List<Event> getUpcomingEvents(User user) {
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusWeeks(5);
        RestTemplate restTemplate = new RestTemplate();
        String queryApi = String.format("&sort=date,asc&startDateTime=%sT00:00:00Z&endDateTime=%sT23:59:59Z&countryCode=GB", now, endDate);
        String url = apiUrl + apiKey + queryApi;
        String response = restTemplate.getForObject(url, String.class);

        List<Event> events = extractEventDetails(response);
        List<UserHistory> userHistory = extractEventDetailsHistory(response);

        if (user != null) {
            saveUserSearchHistory(user, userHistory);
        }
        return events;
    }

    @Override
    public void saveEvent(Event event) {
        favoriteEventRepository.save(event);
    }

    @Override
    public Event getEventByIdAndUser(long eventId, User user) {
        return favoriteEventRepository.findByIdAndUser(eventId, user);
    }

    @Override
    public void deleteEvent(Event eventToDelete) {
        favoriteEventRepository.delete(eventToDelete);
    }

    private List<UserHistory> extractEventDetailsHistory(String jsonResponse) {
        List<UserHistory> events = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode eventsArray = root.path("_embedded").path("events");

            for (JsonNode eventNode : eventsArray) {
                String eventId = eventNode.path("id").asText();
                String eventName = eventNode.path("name").asText();
                String genre = eventNode.path("classifications").path(0).path("genre").path("name").asText(null); // Returns null if path does not exist
                JsonNode venueNode = eventNode.path("_embedded").path("venues").path(0);
                String venueName = venueNode.path("name").asText(null);
                String city = venueNode.path("city").path("name").asText(null);
                String dateTime = parseEventDate(eventNode);

                UserHistory history = new UserHistory();
                history.setEventId(eventId);
                history.setEventName(eventName);
                history.setGenre(genre);
                history.setVenueName(venueName);
                history.setCity(city);
                history.setDateTime(dateTime);

                events.add(history);
            }
        } catch (Exception e) {
            // TODO
        }
        return events;
    }

    private List<Event> extractEventDetails(String jsonResponse) {
        List<Event> events = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode eventsArray = root.path("_embedded").path("events");

            for (JsonNode eventNode : eventsArray) {
                String eventId = eventNode.path("id").asText();
                String eventName = eventNode.path("name").asText();
                String genre = eventNode.path("classifications").path(0).path("genre").path("name").asText(null); // Returns null if path does not exist
                JsonNode venueNode = eventNode.path("_embedded").path("venues").path(0);
                String venueName = venueNode.path("name").asText(null);
                String city = venueNode.path("city").path("name").asText(null);
                String dateTime = parseEventDate(eventNode);
                
                Event event = new Event();
                event.setEventId(eventId);
                event.setEventName(eventName);
                event.setGenre(genre);
                event.setVenueName(venueName);
                event.setCity(city);
                event.setDateTime(dateTime);

                events.add(event);
            }
        } catch (Exception e) {
            // TODO
        }
        return events;
    }

    private void saveUserSearchHistory(User user, List<UserHistory> history) {
        for (UserHistory anEvent : history) {
            anEvent.setUser(user);
            userHistoryRepo.save(anEvent);
        }
    }

    private String parseEventDate(JsonNode eventNode) {
        String dateTimeString = eventNode.path("dates").path("start").path("dateTime").asText(null);

        if (dateTimeString != null) {
            // Extract the date part (YYYY-MM-DD) from the ISO 8601 string
            String datePart = dateTimeString.split("T")[0];

            // Format the date part to the format (dd/MM/yyyy)
            LocalDate localDate = LocalDate.parse(datePart, DateTimeFormatter.ISO_LOCAL_DATE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return localDate.format(formatter);
        }
        return null;
    }


}
