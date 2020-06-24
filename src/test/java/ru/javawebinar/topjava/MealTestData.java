package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

public class MealTestData {
    private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static int MEAL_ID_SEQ = ADMIN_ID + 1;

    public static final Meal USER_MEAL_1 = new Meal(getMealIdSeq(), getLocalDateTime("2020-06-24 08:30"), "Завтрак", 1000);
    public static final Meal USER_MEAL_2 = new Meal(getMealIdSeq(), getLocalDateTime("2020-06-24 13:22"), "Обед", 500);
    public static final Meal USER_MEAL_3 = new Meal(getMealIdSeq(), getLocalDateTime("2020-06-24 22:02"), "Ужин", 800);
    public static final Meal ADMIN_MEAL_1 = new Meal(getMealIdSeq(), getLocalDateTime("2020-06-23 19:20"), "Суп", 700);
    public static final Meal ADMIN_MEAL_2 = new Meal(getMealIdSeq(), getLocalDateTime("2020-06-22 12:33"), "Торт", 1200);
    public static final Meal ADMIN_MEAL_3 = new Meal(getMealIdSeq(), getLocalDateTime("2020-06-20 18:50"), "Печенье", 300);
    public static final Meal ADMIN_MEAL_4 = new Meal(getMealIdSeq(), getLocalDateTime("2020-06-21 13:30"), "Пирог", 900);

    private static int getMealIdSeq() {
        return MEAL_ID_SEQ++;
    }

    public static LocalDateTime getLocalDateTime(String str) {
        return LocalDateTime.parse(str, fmt);
    }

    public static Meal getNew() {
        return new Meal(getLocalDateTime("2020-06-24 12:20"), "Тестовая еда", 1111);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setDescription("Завтра измененный");
        updated.setCalories(999);
        return updated;
    }

    public static List<Meal> getOrderedListByDate() {
        return Arrays.asList(ADMIN_MEAL_4, ADMIN_MEAL_3);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        List<Meal> expectedList = Arrays.asList(expected);
        expectedList.sort(Comparator.comparing(Meal::getDateTime).reversed());
        assertMatch(actual, expectedList);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorOnFields("dateTime", "description", "calories").isEqualTo(expected);
    }
}
