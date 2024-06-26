package com.t3.service;

import com.t3.models.Event;
import com.t3.models.User;

public interface UserService {
    boolean loginUserCheck(String username,String password);
    void registerUserCheck(User user);
    User getUser(String username);
    void deleteUser(String password, String id);
    Event addFavEventToUser(Event event);

}
