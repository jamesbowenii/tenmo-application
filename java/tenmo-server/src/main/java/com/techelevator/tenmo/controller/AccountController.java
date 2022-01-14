package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;


@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    @GetMapping("balance")
    public Accounts showAccountBalance(Principal principal) {
        int id = userDao.findIdByUsername(principal.getName());
        return accountDao.showAccountBalance(id);
    }


}


