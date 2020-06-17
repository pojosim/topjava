package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal get(int id) {
        log.info("Get {}", id);
        return service.get(id, authUserId());
    }

    public List<MealTo> getAll() {
        log.info("GetAll");
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAll(String startDate, String endDate, String startTime, String endTime) {
        List<Meal> meals = service.getAll(authUserId());
        LocalDate startLocalDate = !startDate.isEmpty() ? LocalDate.parse(startDate) : LocalDate.MIN;
        LocalDate endLocalDate = !endDate.isEmpty() ? LocalDate.parse(endDate) : LocalDate.MAX;
        LocalTime startLocalTime = !startTime.isEmpty() ? LocalTime.parse(startTime) : LocalTime.MIN;
        LocalTime endLocalTime = !endTime.isEmpty() ? LocalTime.parse(endTime) : LocalTime.MAX;

        return MealsUtil.getFilteredTos(meals, authUserCaloriesPerDay(), startLocalTime, endLocalTime, startLocalDate, endLocalDate);
    }

    public Meal create(Meal meal) {
        log.info("Create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void update(Meal meal) {
        log.info("Update {}", meal);
        service.update(meal, authUserId());
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }
}