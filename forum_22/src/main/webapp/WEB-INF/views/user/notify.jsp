<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>通知中心</title>
    <link href="//cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
</head>
<body>

<%@ include file="../include/navbar.jsp"%>

<!--header-bar end-->
<div class="container">
    <div class="box">
        <div class="box-header">
            <span class="title"><i class="fa fa-bell"></i> 通知中心</span>
        </div>
        <button id="markBtn" style="margin-left: 8px;" disabled class="btn btn-mini">标记为已读</button>
        <table class="table">
            <thead>
            <tr>

                <th width="30">
                    <c:if test="${not empty requestScope.notifyList}">
                        <input type="checkbox" id="ckFather">
                    </c:if>
                </th>
                <th width="200">发布日期</th>
                <th>内容</th>
            </tr>
            </thead>
            <tbody>

            <c:choose>
                <c:when test="${empty requestScope.notifyList}">
                    <tr>
                        <%--表格中跨行跨列--%>
                        <td colspan="3">暂时没有新的消息</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${requestScope.notifyList}" var="notify">

                        <tr>
                            <td><input value="${notify.id}" type="checkbox" class="ckSon"></td>
                            <td>${notify.createtime}</td>
                            <%--<td>您发布的主题<a>${topic.title}</a>有了新的回复</td>--%>
                            <%--将通知需要的内容封装好，再保存进数据库，可以少做一次topic的查询--%>
                            <td>${notify.content}</td>
                        </tr>

                    </c:forEach>
                </c:otherwise>
            </c:choose>

            </tbody>
        </table>

    </div>
    <!--box end-->
</div>
<!--container end-->
<script src="/static/js/jquery-1.11.1.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script>

</script>
</body>
</html>
