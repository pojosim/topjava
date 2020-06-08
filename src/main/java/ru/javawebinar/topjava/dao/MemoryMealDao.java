package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.dao.mockinmemory.MealInMemoryRepositories;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MemoryMealDao implements MealDao {
    private MealInMemoryRepositories meals = new MealInMemoryRepositories();

    @Override
    public List<Meal> getAllMeals() {
        return meals.getAllMeals();
    }

    @Override
    public Meal getMealById(long mealId) {
        return meals.getMealById(mealId);
    }

    @Override
    public Meal save(Meal meal) {
        return meals.save(meal);
    }

    @Override
    public void deleteMealById(long mealId) {
        meals.deleteMealById(mealId);
    }
}
