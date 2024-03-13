package com.vkRestAPI.services;

import com.vkRestAPI.cache.PostCache;
import com.vkRestAPI.entities.Post;
import com.vkRestAPI.repositories.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {
    private final RestTemplate restTemplate;
    private final PostRepository postRepository;
    private final UserService userService;
    private final PostCache postCache;
    private final String domain = "https://jsonplaceholder.typicode.com";

    public String getAllPosts(){
        String url = domain + "/posts";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String getOnePostById(int id){
        String url = domain + "/posts/" + id;
        if(postCache.takePostFromCache(id) == null){
            postCache.addPostInCache(postRepository.getById(id), id);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        }else {
            return postCache.takePostFromCache(id).toString();
        }

    }

    public String getPostsByUserId(int userId){
        String url = domain + "/users/" + userId + "/posts";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String getPostComments(int id){
        String url = domain + "/posts" + id + "/comments";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String postRequestForPost(String title, String body, int userId){
        String url = domain + "/posts";
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);
        data.put("userId", userId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
        String response;
        try {
            response = restTemplate.postForObject(url, request, String.class);
            System.out.println("All be fine!");
            try {
                register(new Post(title, body, userService.getUserById(userId)));
            }catch (EntityNotFoundException e){
                System.out.println("Post didnt add into DB because user not exists");
            }
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
            return "Error, post didnt add";
        }
        return response;
    }

    public void register(Post post){
        postRepository.save(post);
    }

    public String putRequestForPost(int postId, String title, String body){
        String url = domain + "/posts/" + postId;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);

        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
            System.out.println("Post successfully updated!");
        } catch (HttpClientErrorException e) {
            System.out.println("Error: " + e.getMessage());
            return "Error";
        }
        return response.getBody();
    }

    public String deleteRequestForPost(int postId){
        String url = domain + "/posts" + postId;
        try {
            restTemplate.delete(url);
            return "Post was deleted";
        } catch (HttpClientErrorException e){
            return e.getMessage();
        }
    }
}
