package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    JdbcTemplate jdbcTemplate;
    JdbcAccountDao jdbcAccountDao;
    JdbcUserDao jdbcUserDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcAccountDao jdbcAccountDao, JdbcUserDao jdbcUserDao){
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcUserDao = jdbcUserDao;
    }

    @Override
    public TransferDTO addTransfer(TransferDTO transferDTO, double balance) {

        Accounts accountFrom = jdbcAccountDao.getAccountByUserId(transferDTO.getAccountFrom());
        Accounts accountTo = jdbcAccountDao.getAccountByUserId(transferDTO.getAccountTo());
        transferDTO.setAccountFrom(accountFrom.getAccountId());
        transferDTO.setAccountTo(accountTo.getAccountId());



        if(balance >= transferDTO.getAmount()){
            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
            int transferId = jdbcTemplate.queryForObject(sql, Integer.class, transferDTO.getTransferTypeId(), transferDTO.getTransferStatusId(), transferDTO.getAccountFrom(), transferDTO.getAccountTo(), transferDTO.getAmount());
            jdbcAccountDao.adjustAccountBalances(transferDTO, accountFrom, accountTo);
            transferDTO.setTransferId(transferId);
        }
        return transferDTO;
    }

    @Override
    public List<TransferDTO> viewAllTransfers(Principal principal) {
        List<TransferDTO> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount, u.username FROM " +
                "transfers t JOIN accounts a ON a.account_id = t.account_from " +
                "JOIN users u ON a.user_id = u.user_id WHERE u.username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, principal.getName());
        while (results.next()) {
            TransferDTO transferDTO = new TransferDTO();
            transferDTO.setTransferTypeId(results.getInt("transfer_type_id"));
            transferDTO.setTransferStatusId(results.getInt("transfer_status_id"));
            transferDTO.setAccountFrom(results.getInt("account_from"));
            transferDTO.setAccountTo(results.getInt("account_to"));
            transferDTO.setAmount(results.getDouble("amount"));
            transferDTO.setTransferId(results.getInt("transfer_id"));
            String newSql = "SELECT username FROM users u " +
                    "JOIN accounts a ON a.user_id = u.user_id WHERE account_id = ?";

            SqlRowSet rs = jdbcTemplate.queryForRowSet(newSql, transferDTO.getAccountFrom());
            if (rs.next()) {
                transferDTO.setUsernameFrom(rs.getString("username"));
            }
            rs = jdbcTemplate.queryForRowSet(newSql, transferDTO.getAccountTo());
            if (rs.next()) {
                transferDTO.setUsernameTo(rs.getString("username"));
                transferList.add(transferDTO);
            }
        }
        return transferList;
    }
}
