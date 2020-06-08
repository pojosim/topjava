package ru.javawebinar.topjava.dao.inmemory;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

public class MealInMemoryRepositories implements MealDao {

    private static final Logger log = getLogger(MealInMemoryRepositories.class);
    private AtomicLong counter = new AtomicLong(0);
    private Map<Long, Meal> mealsMemoryDB = new ConcurrentHashMap<>();

    {
        this.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        this.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        this.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        this.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        this.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        this.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        this.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(mealsMemoryDB.values());
    }

    @Override
    public Meal getMealById(long mealId) {
        return mealsMemoryDB.get(mealId);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMemoryDB.put(meal.getId(), meal);
            return meal;
        }

        return mealsMemoryDB.computeIfPresent(meal.getId(), (key, value) -> meal);
    }

    @Override
    public void deleteMealById(long mealId) {
        mealsMemoryDB.remove(mealId);
    }
}
