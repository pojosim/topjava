package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.dao.inmemory.MealInMemoryRepositories;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MemoryMealDao implements MealDao {
    private MealInMemoryRepositories repositories = new MealInMemoryRepositories();

    @Override
    public List<Meal> getAllMeals() {
        return repositories.getAllMeals();
    }

    @Override
    public Meal getMealById(long mealId) {
        return repositories.getMealById(mealId);
    }

    @Override
    public Meal save(Meal meal) {
        return repositories.save(meal);
    }

    @Override
    public void deleteMealById(long mealId) {
        repositories.deleteMealById(mealId);
    }
}
