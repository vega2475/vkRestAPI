package com.vkRestAPI.cache;

import com.vkRestAPI.entities.Post;
import com.vkRestAPI.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PostCache {
    private Map<Integer, Post> map = new HashMap<>();

    public void addPostInCache(Post post, int id){
        map.put(id, post);
    }

    public Post takePostFromCache(int id){
        if(map.containsKey(id)){
            return map.get(id);
        }
        return null;
    }
}
