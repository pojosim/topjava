package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealRepository repository;

    @Autowired
    MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertMatch(meal, USER_MEAL_1);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_2.getId(), USER_ID);
        assertNull(repository.get(USER_MEAL_2.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> expectedMeals = Arrays.asList(ADMIN_MEAL_4, ADMIN_MEAL_3);
        assertMatch(service.getBetweenInclusive(ADMIN_MEAL_3.getDate(), ADMIN_MEAL_4.getDate(), ADMIN_ID), expectedMeals);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, USER_MEAL_1, USER_MEAL_2, USER_MEAL_3);
    }

    @Test
    public void update() {
        Meal meal = MealTestData.getUpdated();
        service.update(meal, USER_ID);
        assertMatch(service.get(meal.getId(), USER_ID), meal);
    }

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, ADMIN_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, ADMIN_ID), newMeal);
    }

    @Test
    public void shouldExpectedNotFoundWhenDeleteWitOtherUserId() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_2.getId(), ADMIN_ID));
    }

    @Test
    public void shouldExpectedNotFoundWhenGetWitOtherUserId() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_2.getId(), ADMIN_ID));
    }

    @Test
    public void shouldExpectedNotFoundWhenUpdateWitOtherUserId() {
        Meal meal = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(meal, ADMIN_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DuplicateKeyException.class, () -> service.create(new Meal(USER_MEAL_3.getDateTime(), "Еда сегодня", 222), USER_ID));
    }
}