package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController extends AbstractMealController {

    public MealRestController(MealService service) {
        super(service);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
    }
}