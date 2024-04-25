package com.example.demo.controller;

import com.example.demo.service.SongService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;

    @GetMapping
    public ResponseEntity<String> getAllSongs() {
        try {
            return ResponseEntity.ok(songService.getAllSongs().toString());
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(
                    "bad request!"
            );
        }
    }

    @GetMapping("/{songId}")
    public ResponseEntity<String> getSongById(@RequestParam String songId) {
        try {
            return ResponseEntity.ok(songService.getSongById(songId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such song found!"
            );
        }
    }

    @PostMapping
    public ResponseEntity<String> createSong(@RequestBody String songDetails) {
        try {
            return ResponseEntity.ok(songService.createSong(songDetails).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "wrong details"
            );
        }
    }

    @PutMapping("/{songId}")
    public ResponseEntity<String> updateSong(@RequestParam String songId, @RequestBody String updatedSongDetails) {
        try {
            return ResponseEntity.ok(songService.updateSong(songId, updatedSongDetails).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such song found"
            );
        }
    }

    @DeleteMapping("/{songId}")
    public void deleteSong(@RequestParam String songId) {
        songService.deleteSong(songId);
    }
}
