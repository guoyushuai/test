<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>找回密码</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
</head>
<body>

<%@ include file="../include/navbar.jsp"%>

<div class="container">
    <div class="box">
        <div class="box-header">
            <span class="title">找回密码</span>
        </div>

        <form action="" class="form-horizontal" id="foundForm">
            
            <div class="control-group">
                <label class="control-label">请选择找回方式</label>
                <div class="controls">
                    <select name="type" id="type">
                        <option value="email">根据电子邮件找回密码</option>
                        <option value="phone">根据手机号码找回密码</option>
                    </select>
                </div>
            </div>
            
            <div class="control-group">
                <%--默认值必须设值，否则空白，且最好与第一个选项相同--%>
                <label class="control-label" id="typename">电子邮件</label>
                <div class="controls">
                    <input type="text" name="value">
                </div>
            </div>
            <div class="form-actions">
                <button class="btn btn-primary" type="button" id="foundBtn">提交</button>
            </div>

        </form>

    </div>
    <!--box end-->
</div>
<!--container end-->
<script src="/static/js/jquery-1.11.1.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script src="/static/js/sweetalert.min.js"></script>
<script src="/static/js/user/foundPassword.js"></script>

</body>
</html>