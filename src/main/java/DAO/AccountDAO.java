package DAO;

import Model.Account;

import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public Account addAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "insert into Account(username, password) values(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()){
                int getAccount_id = (int) pkResultSet.getLong(1);
                return new Account(getAccount_id, account.getUsername(), account.getPassword());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try{
            String sql = "select * from Account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet res = preparedStatement.executeQuery();
            while(res.next()){
                Account account = new Account(res.getInt("account_id"), res.getString("username"), res.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account login(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from Account where account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account.getAccount_id());
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                int account_id = res.getInt("account_id");
                String username = res.getString("username");
                String password = res.getString("password");
                return new Account(account_id, username, password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}