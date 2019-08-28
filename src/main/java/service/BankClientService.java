package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }
    public BankClient getClientByNameAndPassword(String name, String password){
        try {
            BankClient client= getBankClientDAO().getClientByNameAndPassword(name,password);
            return client;
        } catch (SQLException e) {
            e.printStackTrace();
        }
return null;
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) throws DBException {
        try {
            return getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public List<BankClient> getAllClient() throws DBException {
        try {
            return getBankClientDAO().getAllBankClient();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean deleteClient(String name) {
        return false;
    }

    public boolean addClient(BankClient client) throws DBException {
        try {
            String password = client.getPassword();
            String name = client.getName();
            if (getBankClientDAO().getClientByName(name) == null) {
                getBankClientDAO().addClient(client);
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws SQLException, DBException {
        if (getBankClientDAO().validateClient(sender.getName(), sender.getPassword())) {

                if (getBankClientDAO().isClientHasSum(sender.getName(), sender.getMoney())) {
                    BankClient bankClient = getBankClientDAO().getClientByName(name);
                    if (getBankClientDAO().validateClient(bankClient.getName(), bankClient.getPassword())) {
                        if (sender.getPassword().contains(" ")) {
                            return false;
                        }


                        getBankClientDAO().updateClientsMoney(sender.getName(), sender.getPassword(), -value);
                        getBankClientDAO().updateClientsMoney(bankClient.getName(), bankClient.getPassword(), +value);
                        return true;

                }
            }
        }
        return false;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login
                    append("password=root");       //password

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
