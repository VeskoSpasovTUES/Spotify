package com.example.demo.service;

import com.example.demo.entity.Song;
import com.example.demo.repository.SongRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    @Value("${user.service.url}")
    private String userServiceUrl;
    @Autowired
    private SongRepository songRepository;
    private RestTemplate restTemplate;

    public ResponseEntity<String> getAllSongs() throws JsonProcessingException {
        List<Song> songs = songRepository.findAll();

        ObjectMapper mapper = new ObjectMapper();
        String songsJson = mapper.writeValueAsString(songs);
        return new ResponseEntity<>(songsJson, HttpStatus.OK);
    }

    public ResponseEntity<String> getSongById(Long songId) {
        Optional<Song> songOptional = songRepository.findById(songId);
        if (songOptional.isPresent()) {
            Song song = songOptional.get();

            ObjectMapper mapper = new ObjectMapper();
            try {
                String songJson = mapper.writeValueAsString(song);
                return new ResponseEntity<>(songJson, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error converting song to JSON", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Song not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> createSong(String songDetails) {

        ObjectMapper mapper = new ObjectMapper();
        Song newSong;
        try {
            newSong = mapper.readValue(songDetails, Song.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error parsing song details", HttpStatus.BAD_REQUEST);
        }
        Song savedSong;
        try {
            savedSong = songRepository.save(newSong);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error saving song", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            String songJson = mapper.writeValueAsString(savedSong);
            return new ResponseEntity<>(songJson, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error converting song to JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> updateSong(Long songId, String updatedSongDetails) {

        Optional<Song> songOptional = songRepository.findById(songId);
        if (!songOptional.isPresent()) {
            return new ResponseEntity<>("Song not found", HttpStatus.NOT_FOUND);
        }
        Song existingSong = songOptional.get();

        ObjectMapper mapper = new ObjectMapper();
        Song updateDetails;
        try {
            updateDetails = mapper.readValue(updatedSongDetails, Song.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error parsing updated song details", HttpStatus.BAD_REQUEST);
        }

        existingSong.setTitle(updateDetails.getTitle() != null ? updateDetails.getTitle() : existingSong.getTitle());
        existingSong.setArtistId(updateDetails.getArtistId() != null ? updateDetails.getArtistId() : existingSong.getArtistId());

        Song updatedSong;
        try {
            updatedSong = songRepository.save(existingSong);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating song", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            String songJson = mapper.writeValueAsString(updatedSong);
            return new ResponseEntity<>(songJson, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error converting song to JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteSong(Long songId) {
        songRepository.deleteById(songId);
    }

    public List<Song> getUserSongs(String userId) throws JsonProcessingException {
        String url = userServiceUrl + "/" + userId + "/associatedSongs"; // Assuming endpoint in UserService
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            List<Long> userSongIds = mapper.readValue(response.getBody(), List.class);

            List<Song> userSongs = songRepository.findAllById(userSongIds);
            return userSongs;
        } else {
            return new ArrayList<>();
        }
    }

    public void addUserSong(String userId, Long songId) {
        String url = userServiceUrl + "/" + userId + "/addSong/" + songId;
        restTemplate.postForEntity(url, null, null);
    }

    public void removeUserSong(String userId, Long songId) {
        String url = userServiceUrl + "/" + userId + "/removeSong/" + songId;
        restTemplate.delete(url);
    }
}
