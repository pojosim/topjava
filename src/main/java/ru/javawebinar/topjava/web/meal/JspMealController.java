package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        List<MealTo> mealToList = MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
        log.info("getAll for user {}", authUserId());
        model.addAttribute("meals", mealToList);
        return "meals";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        service.delete(id, authUserId());
        log.info("delete meal {} for user {}", id, authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String initCreateForm(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        log.info("init create {} for user {}", meal, authUserId());
        model.addAttribute("meal", meal);
        model.addAttribute("action", "create");
        return "mealForm";
    }

    @GetMapping("/{id}/update")
    public String initUpdateForm(Model model, @PathVariable("id") int id) {
        Meal meal = service.get(id, authUserId());
        log.info("init update {} for user {}", meal, authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        log.info("filter {}", authUserId());
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @PostMapping("/save")
    public String processSave(HttpServletRequest request) throws UnsupportedEncodingException {
//        if (errors.hasErrors()) {
//            return "mealForm";
//        }

        //request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            log.info("process create {} for user {}", meal, authUserId());
            service.create(meal, authUserId());
        } else {
            log.info("process update {} for user {}", meal, authUserId());
            meal.setId(getId(request));
            service.update(meal, authUserId());
        }
        log.info("process update {} for user {}", meal, authUserId());
        service.update(meal, authUserId());
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
