package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDTO;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    public TransferDTO addTransfer(TransferDTO transferDTO, double balance  );

    public List<TransferDTO> viewAllTransfers(Principal principal);
}
