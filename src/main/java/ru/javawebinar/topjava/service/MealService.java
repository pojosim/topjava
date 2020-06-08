package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MemoryMealDao;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealService implements MealDao {
    private MealDao mealDao = new MemoryMealDao();

    @Override
    public List<Meal> getAllMeals() {
        return mealDao.getAllMeals();
    }

    @Override
    public Meal getMealById(long mealId) {
        return mealDao.getMealById(mealId);
    }

    @Override
    public Meal save(Meal meal) {
        return mealDao.save(meal);
    }

    @Override
    public void deleteMealById(long mealId) {
        mealDao.deleteMealById(mealId);
    }
}
