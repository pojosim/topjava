<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>List Meals</title>
    <link href="css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>Meals List</h1>
<h3><a href="index.html">Home</a></h3>
<hr>
<table>
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    <th></th>
    <th></th>
    <c:forEach items="${meals}" var="meal">
        <tr class="${meal.excess ? 'red' : 'green'}">
            <td><c:out value="${f:formatLocalDateTime(meal.dateTime, 'yyyy-MM-dd HH:mm')}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<br>
<h2>Create Meal</h2>
<form method="post" action="meals?action=create">
    <input type="datetime-local" placeholder="Date" name="dateTime">
    <input type="text" placeholder="Description" name="description">
    <input type="number" placeholder="Calories" name="calories">
    <input type="submit" value="Create">
</form>
</body>
</html>
