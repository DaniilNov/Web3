package servlet;

import com.google.gson.Gson;
import exception.DBException;
import model.BankClient;
import service.BankClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankClientService bankClientService = new BankClientService();
        Gson gson = new Gson();
        String json=null;
        if (req.getPathInfo().contains("all")) {
            try {
                json = gson.toJson(bankClientService.getAllClient());
            } catch (DBException e) {
                e.printStackTrace();
            }
        } else {
            try {
                json = gson.toJson(bankClientService.getClientByName(req.getParameter("name")));
            } catch (DBException e) {
                e.printStackTrace();
            }
        }
        resp.getWriter().write(json);
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankClientService bankClientService = new BankClientService();
        try {
            bankClientService.createTable();
            resp.setStatus(200);
        } catch (DBException e) {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankClientService bankClientService = new BankClientService();
        if (req.getPathInfo().contains("all")){
            try {
                bankClientService.cleanUp();
            } catch (DBException e) {
                resp.setStatus(400);
            }
        }
    }
}
