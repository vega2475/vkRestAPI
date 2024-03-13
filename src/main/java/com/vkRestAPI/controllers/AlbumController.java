package com.vkRestAPI.controllers;

import com.vkRestAPI.services.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("")
    public String getAllAlbums(){
        return albumService.getAllAlbums();
    }

    @GetMapping("/{id}")
    public String getOneAlbumById(@PathVariable int id){
        return albumService.getAlbumById(id);
    }

    @PostMapping("")
    public String addAlbum(
            @RequestParam String title,
            @RequestParam int userId
    ){
        return albumService.postRequestForAlbum(title, userId);
    }
}
