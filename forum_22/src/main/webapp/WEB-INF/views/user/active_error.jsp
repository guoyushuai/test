<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>激活失败</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
</head>
<body>

<%@include file="../include/navbar.jsp"%>

<div class="container">
    <div class="box">
        <div class="box-header">
            <span class="title"></i>系统提示</span>
        </div>
        <div class="box-padding">
            <%--获取servlet中的数据 EL表达式是jsp自带的重要组成部分--%>
            <h3>${requestScope.message}，激活失败，请稍后再试<%--或 <a href="">&lt;%&ndash;todo&ndash;%&gt;点击重新发送激活邮件</a>--%></h3>
        </div>
    </div>
</div>

</body>
</html>
