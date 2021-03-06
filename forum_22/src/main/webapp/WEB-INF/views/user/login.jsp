<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户登录</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
    <%--<style>
        #change {
            text-decoration: none;
            display: block;
            width: 160px;
            height: 70px;
        }
    </style>--%>
</head>
<body>

<%@ include file="../include/navbar.jsp"%>

<div class="container">
    <div class="box">
        <div class="box-header">
            <span class="title"><i class="fa fa-sign-in"></i> 登录</span>
        </div>

        <form action="" class="form-horizontal" id="loginForm">

            <%--<c:if test="${not empty requestScope.message}">
                <div class="alert alert-success">${requestScope.message}</div>
            </c:if>--%>

            <c:if test="${param.state == 'logout'}">
                <div class="alert alert-success">你已安全退出</div>
            </c:if>

            <c:if test="${not empty param.redirect}">
                <div class="alert alert-success">请登录后再继续</div>
            </c:if>

            <div class="control-group">
                <label class="control-label">账号</label>
                <div class="controls">
                    <input type="text" name="username" id="username">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">密码</label>
                <div class="controls">
                    <input type="password" name="password">
                </div>
            </div>
                <%--todo 密码错三次再出现验证码比较好 --%>
                <%--<div class="control-group">
                    <label class="control-label"></label>
                    <div class="controls">
                        <a href="javascript:;" id="change">
                            <img src="/captcha.png" id="img" title="看不清？点击更换">
                        </a>
                    </div>
                    <label class="control-label">验证码</label>
                    <div class="controls">
                        <input type="text" name="code">
                    </div>
                </div>--%>

            <div class="control-group">
                <label class="control-label"></label>
                <div class="controls">
                    <a href="/foundPassword">忘记密码</a>
                </div>
            </div>

            <div class="form-actions">
                <button class="btn btn-primary" class="button" id="btn">登录</button>

                <a href="/reg">注册账号</a>
            </div>

        </form>

    </div>
    <!--box end-->
</div>
<!--container end-->
<script src="/static/js/jquery-1.11.1.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script src="/static/js/sweetalert.min.js"></script>
<script src="/static/js/user/login.js"></script>

</body>
</html>