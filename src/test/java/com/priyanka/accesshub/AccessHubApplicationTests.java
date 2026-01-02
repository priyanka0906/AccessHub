package com.priyanka.accesshub;

import com.priyanka.accesshub.controller.StatusController;
import com.priyanka.accesshub.models.User;
import com.priyanka.accesshub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AccessHubApplicationTests {
    @Autowired
    StatusController status;

    @Autowired
    private UserRepository userRepository;

    @Test
     void test(){

        Optional<User> found = userRepository.findByUsername("Pihu");

        assertTrue(found.isPresent(),"User found");
        assertEquals("Pihu",found.get().getUsername());
    }

}
