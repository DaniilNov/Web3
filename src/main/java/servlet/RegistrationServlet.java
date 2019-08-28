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
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    BankClient bankClient;
    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<>();
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.parseLong(req.getParameter("money"));
        bankClient = new BankClient(name, password, money);

        try {
            if (bankClientService.getClientByNameAndPassword(name,password) == null) {
                bankClientService.addClient(bankClient);
                map.put("message", "Add client successful");
            } else {
                map.put("message", "Client not add");
            }
        } catch (DBException e) {
            e.printStackTrace();
        }

        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
