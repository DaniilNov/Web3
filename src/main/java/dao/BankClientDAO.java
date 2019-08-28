package dao;

import com.sun.deploy.util.SessionState;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> clients = new ArrayList<>();
        Statement stmt = connection.createStatement();
        stmt.execute("SELECT * FROM bank_client");
        ResultSet result = stmt.getResultSet();
        while (result.next()) {
            long id = result.getLong(1);
            String names = result.getString(2);
            String password = result.getString(3);
            long money = result.getLong(4);
            BankClient client = new BankClient(id, names, password, money);
            clients.add(client);
        }
        stmt.close();
        result.close();
        return clients;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        BankClient bankClient = getClientByName(name);
        if (bankClient != null) {
            if (bankClient.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {

        if (validateClient(name, password)) {
            BankClient bankClient = getClientByName(name);
            Long resultMoney = bankClient.getMoney() + transactValue;
            PreparedStatement pstmt = connection.prepareStatement("UPDATE bank_client SET money=? WHERE name= ?");
            pstmt.setLong(1, resultMoney);
            pstmt.setString(2, bankClient.getName());
            pstmt.executeUpdate();
            pstmt.close();
        }

    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("SELECT * FROM bank_client WHERE id='" + id + "'");
        ResultSet res = stmt.getResultSet();
        res.next();
        long id2 = res.getLong(1);
        String names = res.getString(2);
        String password = res.getString(3);
        long money = res.getLong(4);
        BankClient client = new BankClient(id2, names, password, money);
        return client;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        BankClient bankClient = getClientByName(name);
        Long sum = bankClient.getMoney();
        if (sum >= expectedSum) {
            return true;
        }
        return false;


    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clien where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM bank_client WHERE name=?");
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();

            result.next();
            String names = result.getString("name");
            String password = result.getString("password");
            Long money = result.getLong("money");
            BankClient client = new BankClient(names, password, money);
            result.close();
            stmt.close();
            return client;
        } catch (SQLException e) {
            return null;
        }
    }

    public void addClient(BankClient client) throws SQLException {
        String name = client.getName();
        if (getClientByName(name) == null) {

            String sql = "INSERT INTO bank_client (name, password, money) VALUES (?,?,?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getPassword());
            pstmt.setLong(3, client.getMoney());
            pstmt.executeUpdate();
            pstmt.close();
        }
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }

    public BankClient getClientByNameAndPassword(String name, String password) throws SQLException {

        String sql = "SELECT * FROM bank_client WHERE name = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
        statement.setString(1, name);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        BankClient bankClient = new BankClient(
                result.getLong("id"),
                result.getString("name"),
                result.getString("password"),
                result.getLong("money"));
        statement.close();
        result.close();
        return bankClient;
    }
}
