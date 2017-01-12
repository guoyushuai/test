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

        <h3>ssm user list</h3>

        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>

        <a href="/user/new" class="btn btn-primary">新增</a>

        <table class="table">
            <thead>
            <tr>
                <th>姓名</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>

            <c:forEach items="${userList}" var="user">
                <tr>
                    <td>${user.username}</td>
                    <td>
                        <%--注意url的设计--%>
                        <a href="/user/${user.id}/edit">编辑</a>
                        <a href="/user/${user.id}/del">删除</a>
                    </td>
                </tr>
            </c:forEach>

            </tbody>
        </table>

    </div>

</body>
</html>
