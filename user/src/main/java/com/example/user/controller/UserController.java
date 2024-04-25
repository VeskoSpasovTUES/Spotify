package com.example.user.controller;

import com.example.user.dto.SongDTO;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @Value("${song.service.url}")
    private String songServiceUrl;
    @Value("${playlist.service.url}")
    private String playlistServiceUrl;

    private final RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<String> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers().toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such users found!"
            );
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> getUserById(@RequestParam String userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user found!"
            );
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody String userDetails) {
        try {
            return ResponseEntity.ok(userService.createUser(userDetails).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "bad request"
            );
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@RequestParam String userId, @RequestBody String updatedUserDetails) {
        try {
            return ResponseEntity.ok(userService.updateUser(userId, updatedUserDetails).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user found!"
            );
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@RequestParam String userId) {
        userService.deleteUser(userId);
    }

    // Functional endpoints
    @GetMapping("/{userId}/playlists")
    public ResponseEntity<String> getUserPlaylists(@RequestParam String userId) {
        try {
            return ResponseEntity.ok(userService.getUserPlaylists(userId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user found!"
            );
        }
    }

    @PostMapping("/{userId}/playlists/{playlistId}")
    public ResponseEntity<String> addUserPlaylist(@RequestParam String userId, @RequestParam String playlistId) {
        try {
            return ResponseEntity.ok(userService.addUserPlaylist(userId, playlistId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user or playlist found!"
            );
        }
    }

    @DeleteMapping("/{userId}/playlists/{playlistId}")
    public void removeUserPlaylist(@RequestParam String userId, @RequestParam String playlistId) {
        userService.removeUserPlaylist(userId, playlistId);
    }

    @GetMapping("/{userId}/songs")
    public ResponseEntity<String> getUserSongs(@RequestParam String userId) {
        try {
            return ResponseEntity.ok(userService.getUserSongs(userId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user found!"
            );
        }
    }

    @PostMapping("/{userId}/songs/{songId}")
    public ResponseEntity<String> addUserSong(@RequestParam String userId, @RequestParam String songId) {
        try {
            return ResponseEntity.ok(userService.addUserSong(userId, songId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user or song found!"
            );
        }
    }

    @DeleteMapping("/{userId}/songs/{songId}")
    public void removeUserSong(@RequestParam String userId, @RequestParam String songId) {
        userService.removeUserSong(userId, songId);
    }

    @GetMapping("/{userId}/artists")
    public ResponseEntity<String> getUserArtists(@RequestParam String userId) {
        try {
            return ResponseEntity.ok(userService.getUserArtists(userId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user found!"
            );
        }
    }

    @PostMapping("/{userId}/artists/{artistId}")
    public ResponseEntity<String> addUserArtist(@RequestParam String userId, @RequestParam String artistId) {
        try {
            return ResponseEntity.ok(userService.addUserArtist(userId, artistId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user or artist found!"
            );
        }
    }

    @DeleteMapping("/{userId}/artists/{artistId}")
    public void removeUserArtist(@RequestParam String userId, @RequestParam String artistId) {
        userService.removeUserArtist(userId, artistId);
    }

    @PostMapping("/{userId}/playlists/{playlistId}/songs/{songId}")
    public ResponseEntity<String> addSongToPlaylist(@RequestParam String userId, @RequestParam String playlistId, @RequestParam String songId) {
        try {
            return ResponseEntity.ok(userService.addSongToPlaylist(userId, playlistId, songId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user or playlist or song found!"
            );
        }
    }

    @DeleteMapping("/{userId}/playlists/{playlistId}/songs/{songId}")
    public void removeSongFromPlaylist(@RequestParam String userId, @RequestParam String playlistId, @RequestParam String songId) {
        userService.removeSongFromPlaylist(userId, playlistId, songId);
    }

    @GetMapping("/{userId}/playlists/{playlistId}/songs")
    public ResponseEntity<String> getPlaylistSongs(@RequestParam String userId, @RequestParam String playlistId) {
        try {
            return ResponseEntity.ok(userService.getPlaylistSongs(userId, playlistId).toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    "no such user or playlist found!"
            );
        }
    }
}
