package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Accounts;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;


    public AccountService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Accounts showAccountBalance() {
        ResponseEntity<Accounts> response = restTemplate.exchange(baseUrl + "balance", HttpMethod.GET, makeAuthEntity(), Accounts.class);
        return response.getBody();


    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}
