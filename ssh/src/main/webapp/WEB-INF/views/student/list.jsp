<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="/static/bootstrap/css/bootstrap.min.css">
</head>
<body>

<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>姓名</th>
            <th>性别</th>
            <th>年龄</th>
            <th>年级</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${studentList}" var="student">
            <tr>
                <td><a href="/student/view/${student.id}">${student.stuname}</a></td>
                <td>${student.sex}</td>
                <td>${student.age}</td>
                <td>${student.date}</td>
            </tr>
        </c:forEach>

        </tbody>
    </table>
</div>

</body>
</html>
