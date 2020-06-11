package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.inmemory.MealInMemoryRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealInMemoryRepository mealRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        mealRepository = new MealInMemoryRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET request processing");
        String action = req.getParameter("action");
        long id;

        switch (action != null ? action : "null") {
            case "delete":
                id = Long.parseLong(req.getParameter("id"));
                mealRepository.deleteById(id);
                log.debug("Delete meal, id={}",id);
                resp.sendRedirect("meals");
                log.debug("Redirect to meals page");
                break;
            case "update":
                id = Long.parseLong(req.getParameter("id"));
                Meal meal = mealRepository.getById(id);
                log.debug("Redirect to form update meal, id={}",id);
                req.setAttribute("meal", meal);
                req.getRequestDispatcher("/meal-edit.jsp").forward(req, resp);
                break;
            case "create":
            default:
                req.setAttribute("meals", MealsUtil.filteredByStreams(mealRepository.getAll(), LocalTime.MIN,
                        LocalTime.MAX, MealsUtil.DEFAULT_CALORIES));
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.debug("POST request processing");
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        switch (action != null ? action : "null") {
            case "create":
            case "update":
                Long id = req.getParameter("id") != null ? Long.parseLong(req.getParameter("id")) : null;
                Meal meal = new Meal(id, LocalDateTime.parse(req.getParameter("dateTime")),
                        req.getParameter("description"), Integer.parseInt(req.getParameter("calories")));
                mealRepository.save(meal);

                if (id == null) {
                    log.debug("Create meal, dateTime={}; description={}; calories={}", meal.getDateTime(), meal.getDescription(), meal.getCalories());
                } else {
                    log.debug("Update meal, id={}; dateTime={}; description={}; calories={}", id, meal.getDateTime(), meal.getDescription(), meal.getCalories());
                }
                break;
        }
        resp.sendRedirect("meals");
    }
}
