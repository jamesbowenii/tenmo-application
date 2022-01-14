package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.TransferDTO;
import java.util.List;

public interface AccountDao {

    public Accounts showAccountBalance(int accountId);

    public List<Double> adjustAccountBalances(TransferDTO transferDTO, Accounts fromAccount, Accounts toAccount);

    public Accounts getAccountByUserId(int userId);
}
