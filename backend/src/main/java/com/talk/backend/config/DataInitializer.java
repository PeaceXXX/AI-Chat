package com.talk.backend.config;

import com.talk.backend.model.User;
import com.talk.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create some test users if they don't exist
        if (userRepository.count() == 0) {
            createTestUsers();
        }
    }
    
    private void createTestUsers() {
        User user1 = new User("test_google_id_1", "alice@example.com", "Alice Johnson", "https://example.com/avatar1.jpg");
        user1.setPhoneNumber("+1234567890");
        userRepository.save(user1);
        
        User user2 = new User("test_google_id_2", "bob@example.com", "Bob Smith", "https://example.com/avatar2.jpg");
        user2.setPhoneNumber("+1234567891");
        userRepository.save(user2);
        
        User user3 = new User("test_google_id_3", "charlie@example.com", "Charlie Brown", "https://example.com/avatar3.jpg");
        user3.setPhoneNumber("+1234567892");
        userRepository.save(user3);
        
        User user4 = new User("test_google_id_4", "diana@example.com", "Diana Prince", "https://example.com/avatar4.jpg");
        user4.setPhoneNumber("+1234567893");
        userRepository.save(user4);
        
        System.out.println("Test users created successfully!");
    }
} 