package com.vkRestAPI.controllers;

import com.vkRestAPI.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("")
    public String getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public String getOnePostById(@PathVariable int id){
        return postService.getOnePostById(id);
    }

    @PostMapping("")
    public String addPost(
            @RequestParam String title,
            @RequestParam String body,
            @RequestParam int userId
    ){
        return postService.postRequestForPost(title, body, userId);
    }

    @PutMapping("/{postId}")
    public String updatePost(
            @RequestParam String title,
            @RequestParam String body,
            @PathVariable int postId
    ){
        return postService.putRequestForPost(postId, title, body);
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable int postId){
        return postService.deleteRequestForPost(postId);
    }
}
