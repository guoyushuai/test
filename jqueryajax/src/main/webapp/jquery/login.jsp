
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <form id="loginForm">

            <div class="form-group">
                <label>账号</label>
                <input type="text" name="username" id="username" class="form-control">
            </div>
            <div class="form-group">
                <label>密码</label>
                <input type="password" name="password" id="password" class="form-control">
            </div>

            <div class="form-group">
                <button class="btn btn-primary" type="button" id="loginBtn">登录</button>
            </div>

        </form>
    </div>
    <script src="/static/js/jquery-1.12.4.min.js"></script>
    <script>
        $(function(){
            $("#loginBtn").click(function () {

                var username = $("#username").val();
                var password = $("#password").val();

                $.post("/login",$("#loginForm").serialize()).done(function (data) {
                    if(data.state == "error") {
                        alert(data.message);
                    } else {
                        alert("登录成功");
                        window.location.href = "/jquery/jq.jsp";
                    }
                }).error(function () {
                    alert("服务器异常")
                });
            });
        });
    </script>


</body>
</html>
