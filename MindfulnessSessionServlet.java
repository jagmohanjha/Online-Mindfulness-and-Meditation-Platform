package com.guvi.mindfulness.servlet;

import com.guvi.mindfulness.dao.MindfulnessSessionDAO;
import com.guvi.mindfulness.exception.ValidationException;
import com.guvi.mindfulness.model.MindfulnessSession;
import com.guvi.mindfulness.service.MindfulnessSessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet that demonstrates GET + POST handling with the service layer.
 */
@WebServlet(name = "MindfulnessSessionServlet", urlPatterns = "/api/sessions")
public class MindfulnessSessionServlet extends HttpServlet {

    private transient MindfulnessSessionService sessionService;

    @Override
    public void init() throws ServletException {
        this.sessionService = new MindfulnessSessionService(new MindfulnessSessionDAO());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        MindfulnessSession session = new MindfulnessSession();
        session.setUserId(Long.parseLong(req.getParameter("userId")));
        session.setTitle(req.getParameter("title"));
        session.setDescription(req.getParameter("description"));
        session.setCategory(req.getParameter("category"));
        session.setDifficulty(req.getParameter("difficulty"));
        session.setScheduledAt(LocalDateTime.parse(req.getParameter("scheduledAt")));
        session.setDurationMinutes(Integer.parseInt(req.getParameter("durationMinutes")));
        session.setReflectionNotes(req.getParameter("reflectionNotes"));
        try {
            long id = sessionService.scheduleSession(session);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Session scheduled\",\"sessionId\":" + id + "}");
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Internal error\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        long userId = Long.parseLong(req.getParameter("userId"));
        List<MindfulnessSession> sessions = sessionService.sessionsForUser(userId);
        String payload = sessions.stream()
                .map(s -> """
                        {
                          "id":%d,
                          "title":"%s",
                          "category":"%s",
                          "durationMinutes":%d
                        }
                        """.formatted(s.getId(), s.getTitle(), s.getCategory(), s.getDurationMinutes()))
                .collect(Collectors.joining(",", "[", "]"));
        resp.getWriter().write(payload);
    }
}

