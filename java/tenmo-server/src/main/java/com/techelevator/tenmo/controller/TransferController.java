package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    @Autowired
    TransferDao transferDao;

    @GetMapping("transfer")
    public List<TransferDTO> viewAllTransfers(Principal principal) {

        return transferDao.viewAllTransfers(principal);
    }

    @PostMapping("transfer")
    public TransferDTO addTransfer(@RequestBody TransferDTO transferDTO, Principal principal) {
        int id = userDao.findIdByUsername(principal.getName());
        double balance = accountDao.showAccountBalance(id).getBalance();
        return transferDao.addTransfer(transferDTO, balance);
    }
}
