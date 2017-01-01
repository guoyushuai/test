<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">
</head>
<body>

<%@ include file="../include/admin_navbar.jsp"%>

<!--header-bar end-->
<div class="container-fluid" style="margin-top:20px">
    <table class="table">
        <thead>
        <tr>
            <th>账号</th>
            <th>注册时间</th>
            <th>最后登录时间</th>
            <th>最后登录IP</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>

            <c:forEach items="${requestScope.page.items}" var="adminuservo">
                <tr>
                    <td>${adminuservo.username}</td>
                    <td>${adminuservo.createtime}</td>
                    <td>${adminuservo.lastlogintime}</td>
                    <td>${adminuservo.lastloginip}</td>
                    <td>
                        <a href="javascript:;" class="update" rel="${adminuservo.userid}">${adminuservo.state == '1'?'禁用':'恢复'}</a>
                    </td>
                </tr>
            </c:forEach>


        </tbody>
    </table>

    <div class="pagination pagination-mini pagination-centered">
        <ul id="pagination" style="margin-bottom:20px;"></ul>
    </div>

</div>
<!--container end-->
<script src="/static/js/jquery-1.11.1.js"></script>
<script src="/static/js/sweetalert.min.js"></script>
<script src="/static/js/jquery.twbsPagination.min.js"></script>
<script>
    $(function () {
        $("#pagination").twbsPagination({
            totalPages:${page.totalPage},
            visiblePages:5,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href: '?p={{number}}&_=3'
        });

        $(".update").click(function () {
            //或者以","分隔多个属性值，在服务端再用字符串的split方法分隔成字符串数组，再分别取值
            var userid = $(this).attr("rel");

            $.ajax({
                url:"/admin/user",
                type:"post",
                data:{"userid":userid},
                success:function (result) {
                    if(result.state == "success") {
                        swal({
                            title: "修改成功",
                            text: "",
                            type: "success"
                        },function () {
                            window.history.go(0);
                        });
                    } else {
                        swal("Oops!", result.message, "error");
                    }
                },
                error:function () {
                    swal("Oops!", "服务器错误", "error");
                }
            });
        });
    });

</script>
</body>
</html>
