
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
</head>
<body>

    <form action="" enctype="application/x-www-form-urlencoded"></form>
    <input type="text" id="name">
    <button id="btn">send request</button>
    <%--<div id="result"></div>--%>

    <script src="/static/js/ajax.js"></script>
    <script>

        (function(){
            document.querySelector("#btn").onclick = function () {

                var name = document.querySelector("#name").value;

                ajax.sendPost({

                    url:"/ajax",
                    data:"name=" + name,
                    success:function (data) {
                        alert(data);
                    },
                    error:function () {
                        alert("服务器异常")
                    }


                })
            }
        })();

    </script>

</body>
</html>
