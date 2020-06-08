<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Update Meal</title>
</head
<h1>Update Meal</h1>
<h3><a href="meals">Back</a></h3>
<hr>
<body>
<form method="POST" action="meals?action=update">
    <input type="hidden" name="id" value="${meal.id}">
    <div> Date : <input type="datetime-local" name="dateTime" value="${meal.dateTime}"/> </div>
    <div> Description : <input type="text" name="description" value="${meal.description}"/> </div>
    <div> Calories : <input type="number" name="calories" value="${meal.calories}"/> </div>
    <div> <input type="submit" value="Update"> </div>
</form>
</body>
</html>
