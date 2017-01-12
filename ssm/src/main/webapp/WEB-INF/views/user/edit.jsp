
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
    <form method="post">
        <input type="hidden" name="id" value="${user.id}">
        <div class="from-group">
            <label>账号</label>
            <input type="text" name="username" value="${user.username}" class="form-control">
        </div>
        <div class="form-group">
            <label>密码</label>
            <input type="password" name="password" class="form-control">
        </div>
        <div class="from-group">
            <button class="btn btn-success">修改</button>
        </div>
    </form>
</div>

</body>
</html>
