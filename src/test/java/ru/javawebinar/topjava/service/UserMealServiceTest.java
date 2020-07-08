package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

@ActiveProfiles(profiles = {Profiles.DATAJPA})
public class UserMealServiceTest extends AbstractServiceTest{

    private final UserService userService;
    private final MealService mealService;
    private final CacheManager cacheManager;

    @Autowired
    public UserMealServiceTest(UserService userService, MealService mealService, CacheManager cacheManager) {
        this.userService = userService;
        this.mealService = mealService;
        this.cacheManager = cacheManager;
    }

    @Before
    public void setUp() throws Exception {
        cacheManager.getCache("users").clear();
    }

    @Test
    public void getUserAndMeals() throws Exception {
        User user = userService.get(UserTestData.USER_ID);
        List<Meal> mealList = mealService.getAll(user.getId());
        MealTestData.MEAL_MATCHER.assertMatch(mealList, MealTestData.MEALS);
    }
}
