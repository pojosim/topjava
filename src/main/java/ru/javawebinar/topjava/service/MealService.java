package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        ValidationUtil.checkNew(meal);
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        checkNotFound(repository.save(meal, userId), "meal=" + meal + ", userId=" + userId);
    }

    public void delete(int id, int userId) {
        checkNotFound(repository.delete(id, userId), "meal=" + id);
    }

    public Meal get(int id, int userId) {
        return checkNotFound(repository.get(id, userId), "meal=" + id + ", userId=" + userId);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

}