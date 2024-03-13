package com.vkRestAPI.services;

import com.vkRestAPI.entities.Album;
import com.vkRestAPI.entities.Post;
import com.vkRestAPI.repositories.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final AlbumRepository albumRepository;
    private final String domain = "https://jsonplaceholder.typicode.com";
    public String getAllAlbums(){
        String url = domain + "/albums";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String getAlbumById(int id){
        String url = domain + "/albums" + id;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String getAlbumsByUserId(int userId){
        String url = domain + "/users" + userId + "/albums";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String postRequestForAlbum(String title, int userId){
        String url = domain + "/albums";
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("userId", userId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
        String response;
        try {
            response = restTemplate.postForObject(url, request, String.class);
            System.out.println("All be fine!");
            try {
                register(new Album(title, userService.getUserById(userId)));
            }catch (EntityNotFoundException e){
                System.out.println("Album didnt add into DB because user not exists");
            }
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
            return "Error, album didnt add";
        }
        return response;
    }

    private void register(Album album){
        albumRepository.save(album);
    }
}
