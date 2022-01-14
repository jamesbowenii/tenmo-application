package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Accounts showAccountBalance(int userId) {
        Accounts account = null;
        String sql = "SELECT account_id, accounts.user_id, balance FROM accounts " +
                "JOIN users ON users.user_id = accounts.user_id " +
                "WHERE users.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while(results.next()) {
            account = mapRowToAccount(results);
            }
        return account;
    }

    @Override
    public List<Double> adjustAccountBalances(TransferDTO transferDTO, Accounts fromAccount, Accounts toAccount) {
        List<Double> adjustmentList = new ArrayList<>();
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";


        double senderBalance = jdbcTemplate.update(sql, fromAccount.getBalance() - transferDTO.getAmount(), transferDTO.getAccountFrom());
        double receiverBalance = jdbcTemplate.update(sql, toAccount.getBalance() + transferDTO.getAmount(), transferDTO.getAccountTo());
        adjustmentList.add(senderBalance);
        adjustmentList.add(receiverBalance);
        return adjustmentList;
    }

    @Override
    public Accounts getAccountByUserId(int userId){
       Accounts account = null;
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
       SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
       while(results.next()){
           account = mapRowToAccount(results);
       }

       return account;
    }



    private Accounts mapRowToAccount(SqlRowSet rs) {
        Accounts account = new Accounts();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}
