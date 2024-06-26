package com.t3.service;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTests {

    private UserServiceImpl service;

    public UserServiceTests(){
        UserRepositoryStub stub = new UserRepositoryStub();
        service = new UserServiceImpl(stub);
    }

    @Test
    public void findUserByUsernameTest(){
        String username = "bilal";
        assertEquals(username,service.getUser(username).getUsername());
        assertNull(service.getUser("poop"));
    }

}
