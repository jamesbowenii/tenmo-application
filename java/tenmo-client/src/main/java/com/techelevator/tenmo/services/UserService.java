package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    public UserService(String url){
        this.baseUrl = url;

    }

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public List<User> findAllExceptCurrentUser(){
        ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, makeAuthEntity(), User[].class);
        return new ArrayList<User>(Arrays.asList(response.getBody()));
    }

    public User findById(int id){
        User user = null;
        ResponseEntity<User> response = restTemplate.exchange(baseUrl + "users/" + id, HttpMethod.GET, makeAuthEntity(), User.class);
        if(response != null){
            return response.getBody();
        }
        return user;
    }
    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
