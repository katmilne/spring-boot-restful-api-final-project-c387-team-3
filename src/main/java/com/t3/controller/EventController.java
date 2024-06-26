package com.t3.controller;

import com.t3.models.Event;
import com.t3.models.User;
import com.t3.service.CustomUserDetailsService;
import com.t3.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/")
    public String getUpcomingEvents(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = null;
        if (auth.isAuthenticated() && !"anonymousUser".equals(username)) {
            user = customUserDetailsService.getUserByUsername(username);
        }

        List<Event> events = eventService.getUpcomingEvents(user);
        model.addAttribute("events", events);
        model.addAttribute("username", user != null ? user.getUsername() : "Guest");
        return "events";
    }

}
