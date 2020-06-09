package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    List<Meal> getAll();
    Meal getById(long mealId);
    Meal save(Meal meal);
    void deleteById(long mealId);
}
