package com.vkRestAPI.services;

import com.vkRestAPI.entities.User;
import com.vkRestAPI.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    public String getAllUsers(){
        String url = "https://jsonplaceholder.typicode.com/users";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String getOneUserById(int userId){
        String url = "https://jsonplaceholder.typicode.com/users/" + userId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public User getUserById(int userId){
        return userRepository.findById(userId).orElseThrow();
    }


}
