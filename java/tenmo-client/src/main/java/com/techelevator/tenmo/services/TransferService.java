package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferDTO addTransfer(int fromId, int toId, double amount) {
        TransferDTO transferDTO = new TransferDTO();
        // fromId and toId initially loaded with user ID values and then these are
        // replaced with account IDs for the respective user
        // when the server receives the transfer request
        transferDTO.setAccountFrom(fromId);
        transferDTO.setAccountTo(toId);
        transferDTO.setAmount(amount);
        transferDTO.setTransferStatusId(2);
        transferDTO.setTransferTypeId(2);
        TransferDTO response = restTemplate.postForObject(baseUrl + "transfer", makeTransferEntity(transferDTO), TransferDTO.class);

        return response;
    }

    public List<TransferDTO> viewAllTransfers() {
        List<TransferDTO> transferList = new ArrayList<>();
        ResponseEntity<TransferDTO[]> response = restTemplate.exchange(baseUrl + "transfer", HttpMethod.GET, makeAuth(), TransferDTO[].class);
        return new ArrayList<TransferDTO>(Arrays.asList(response.getBody()));
    }

    private HttpEntity<TransferDTO> makeTransferEntity(TransferDTO transferDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transferDTO, headers);
    }

    private HttpEntity<TransferDTO> makeAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
