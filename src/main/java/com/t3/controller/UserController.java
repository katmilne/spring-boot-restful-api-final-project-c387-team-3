package com.t3.controller;

import com.t3.models.Event;
import com.t3.models.User;
import com.t3.models.UserHistory;
import com.t3.service.CustomUserDetailsService;
import com.t3.service.EventService;
import com.t3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        userService.registerUserCheck(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/events/favorite")
    public String addFavoriteEvent(@RequestParam String eventId, @RequestParam String eventName,
                                   @RequestParam String city, @RequestParam String venueName,
                                   @RequestParam String genre,@RequestParam String dateTime,
                                   @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUser(userDetails.getUsername());
        Event favoriteEvent = new Event();
        favoriteEvent.setEventId(eventId);
        favoriteEvent.setEventName(eventName);
        favoriteEvent.setCity(city);
        favoriteEvent.setVenueName(venueName);
        favoriteEvent.setGenre(genre);
        favoriteEvent.setDateTime(dateTime); // LocalDate
        favoriteEvent.setUser(user);

        eventService.saveEvent(favoriteEvent);
        return "redirect:/"; // Redirect to the events page after adding to favorites
    }

    @PostMapping("/history")
    public String addFavoriteEventFromHistory(@RequestParam String eventId, @RequestParam String eventName,
                                   @RequestParam String city, @RequestParam String venueName,
                                   @RequestParam String genre, @RequestParam String dateTime,
                                   @AuthenticationPrincipal UserDetails userDetails) {

        // Fetch the user details from the repository
        User user = userService.getUser(userDetails.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Create a new Event object and populate its fields
        Event favoriteEvent = new Event();
        favoriteEvent.setEventId(eventId);
        favoriteEvent.setEventName(eventName);
        favoriteEvent.setCity(city);
        favoriteEvent.setVenueName(venueName);
        favoriteEvent.setGenre(genre);
        favoriteEvent.setDateTime(dateTime);
        favoriteEvent.setUser(user);

        // Save the favorite event to the repository
        eventService.saveEvent(favoriteEvent);

        // Redirect to the history page
        return "redirect:/history";
    }

    @GetMapping("/favorite/events")
    public String getFavoriteEvents(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        if (user != null) {
            Set<Event> events = user.getFavoriteEvents();
            model.addAttribute("events", events);
            model.addAttribute("username", user.getUsername());

        } else {
            model.addAttribute("events", new HashSet<Event>());
            model.addAttribute("username", "Guest");
        }
        return "user";
    }

    @GetMapping("/history")
    public String getUserHistory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = null;
        if (auth.isAuthenticated() && !"anonymousUser".equals(username)) {
            user = customUserDetailsService.getUserByUsername(username);
        }
        if (user != null) {
            Set<UserHistory> events = user.getUserHistory();
            model.addAttribute("events", events);
            model.addAttribute("username", user.getUsername());

        } else {
            model.addAttribute("events", new HashSet<Event>());
            model.addAttribute("username", "Guest");
        }
        return "userHistory";
    }

    @PostMapping("/delete/favorite/events/{eventId}")
    public String removeFavoriteEvent(@PathVariable Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        Event eventToDelete = eventService.getEventByIdAndUser(eventId, user);
        if (eventToDelete != null) {
            eventService.deleteEvent(eventToDelete);
        }
        return "redirect:/favorite/events";
    }

}
