
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <button id="btn">load json</button>
    <script src="/static/js/jquery-1.12.4.min.js"></script>
    <script>
        $(function () {

            $("#btn").click(function () {

                /*$.get("/data.json").done(function (data) {

                    for(var i = 0;i < data.length;i ++ ) {

                        var user = data[i];
                        alert(user.username + " -> " + user.address);

                    }

                }).error(function () {

                    alert("服务器异常")

                });*/

                $.getJSON("/data.json").done(function (data) {

                    for(var i = 0 ; i < data.length;i++) {
                        var user = data[i];
                        alert(user.username + " -> " + user.address);
                    }

                }).error(function () {

                    alert("服务器异常");

                });

            })

        })
    </script>


</body>
</html>
