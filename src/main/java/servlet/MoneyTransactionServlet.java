package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();
    BankClient bankClient = new BankClient();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
        resp.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,Object>map = new HashMap<>();
        String senderName = req.getParameter("senderName");
        String sendrPas = req.getParameter("senderPas");
        Long count = Long.parseLong(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");
        bankClient = new BankClient(senderName,sendrPas,count);
        try {
            if (bankClientService.sendMoneyToClient(bankClient,nameTo,count)){
                map.put("message","The transaction was successful");
            }
            else {
                map.put("message","transaction rejected");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBException e) {
            e.printStackTrace();
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
