package com.guvi.mindfulness.servlet;


import com.guvi.mindfulness.dao.UserDAO;
import com.guvi.mindfulness.exception.ValidationException;
import com.guvi.mindfulness.model.User;
import com.guvi.mindfulness.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Minimal servlet that shows how HTTP requests interact with the service/DAO layer.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/api/register")
public class RegisterServlet extends HttpServlet {

    private transient UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService(new UserDAO());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        User user = new User();
        user.setFullName(req.getParameter("fullName"));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));
        user.setFocusArea(req.getParameter("focusArea"));
        try {
            long id = userService.registerUser(user);
            resp.getWriter().write("{\"message\":\"User registered\",\"userId\":" + id + "}");
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Internal error\"}");
        }
    }
}

