package org.fasttrackit.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fasttrackit.domain.ToDoItem;
import org.fasttrackit.persistence.ToDoItemRepository;
import org.fasttrackit.service.ToDoItemService;
import org.fasttrackit.transfer.SaveToDoItemRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/to-do-items")
public class ToDoItemServlet extends HttpServlet {

    private ToDoItemService toDoItemService = new ToDoItemService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        setAccessControlHeaders(resp);

        ObjectMapper objectMapper = new ObjectMapper();
        SaveToDoItemRequest saveToDoItemRequest = objectMapper.readValue(req.getReader(), SaveToDoItemRequest.class);

        try {
            toDoItemService.createToDoItem(saveToDoItemRequest);
        } catch (Exception e) {
            resp.sendError(500, "Internal error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        setAccessControlHeaders(resp);

        try {
            List<ToDoItem> toDoItems = toDoItemService.getToDoItems();

            //serialising or marshalling - transformarea unui obiect intr-un String
            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = objectMapper.writeValueAsString(toDoItems);

            //content type or mime type
            resp.setContentType("application/json");
            resp.getWriter().print(responseJson);
            resp.getWriter().flush();

        } catch (Exception e) {
            resp.sendError(500,"There was an error processing your request. " + e.getMessage()); //status code = 404 not found
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);

        ObjectMapper objectMapper = new ObjectMapper();
        SaveToDoItemRequest saveToDoItemRequest = objectMapper.readValue(req.getReader(), SaveToDoItemRequest.class);

        try {
            toDoItemService.updateToDoItem(saveToDoItemRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);

        String id = req.getParameter("id");

        try {
            toDoItemService.deleteToDoItem(Long.parseLong(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //for Preflight requests
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}

