package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    List<Meal> getAllMeals();
    Meal getMealById(long mealId);
    Meal save(Meal meal);
    void deleteMealById(long mealId);
}
