package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final MealService mealService = new MealService();

    private static String LIST_MEALS = "listmeals.jsp";
    private static String INSERT_OR_EDIT = "meal.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Redirect to meals page");

        String forward = "";
        String action = req.getParameter("action");
        Long id;

        switch (action == null ? "null" : action) {
            case "delete":
                forward = LIST_MEALS;
                id = Long.valueOf(req.getParameter("id"));
                mealService.deleteMealById(id);
                req.setAttribute("meals", MealsUtil.filteredByStreamsWithoutDate(mealService.getAllMeals(),
                        MealsUtil.DEFAULT_CALORIES));
                break;
            case "update":
                forward = INSERT_OR_EDIT;
                id = Long.valueOf(req.getParameter("id"));
                Meal meal = mealService.getMealById(id);
                req.setAttribute("meal", meal);
                break;
            case "create":
            case "null":
            default:
                forward = LIST_MEALS;
                req.setAttribute("meals", MealsUtil.filteredByStreamsWithoutDate(mealService.getAllMeals(),
                        MealsUtil.DEFAULT_CALORIES));
        }

        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        Meal meal;

        if (action != null && action.equalsIgnoreCase("update")) {
            meal = new Meal(Long.parseLong(req.getParameter("id")), LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"), Integer.parseInt(req.getParameter("calories")));
            mealService.save(meal);
        } else if (action != null && action.equalsIgnoreCase("create")) {
            meal = new Meal(LocalDateTime.parse(req.getParameter("dateTime")),
                    req.getParameter("description"), Integer.parseInt(req.getParameter("calories")));
            mealService.save(meal);
        }

        resp.sendRedirect("meals");
    }
}
