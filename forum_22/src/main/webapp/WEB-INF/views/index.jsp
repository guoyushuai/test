<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
</head>
<body>

<%@include file="include/navbar.jsp"%>

<div class="container">
    <div class="box">
        <div class="talk-item">
            <ul class="topic-type unstyled inline" style="margin-bottom:0px;">
                <li class="${empty requestScope.nodeid ? 'active' : ''}"><a href="/home">全部</a></li>
                <%--注意class中active加引号--%>
                <c:forEach items="${requestScope.nodeList}" var="node">
                    <%--//选择分节点时，向服务器传递节点nodeid，以进一步确定显示响应节点内容--%>
                    <li class="${requestScope.nodeid == node.id ? 'active' : ''}"><a href="/home?nodeid=${node.id}">${node.nodename}</a></li>
                </c:forEach>

            </ul>
        </div>

        <c:forEach items="${requestScope.page.items}" var="topic">
            <div class="talk-item">
                <table class="talk-table">
                    <tr>
                        <td width="50">
                            <img class="avatar" src="http://oi0nv2rk1.bkt.clouddn.com/${topic.user.avatar}?imageView2/1/w/40/h/40" alt="">
                        </td>
                        <td width="80">
                            <a href="">${topic.user.username}</a>
                        </td>
                        <td width="auto">
                            <%--{topic.id显示的是user.id}--%>
                            <a href="/topicDetail?topicid=${topic.id}">${topic.title}</a>
                        </td>
                        <td width="50" align="center">
                            <span class="badge">${topic.replynum}</span>
                        </td>
                    </tr>
                </table>
            </div>
        </c:forEach>

        <%--页签--%>
        <div class="pagination pagination-mini pagination-centered">
            <ul id="pagination" style="margin-bottom:20px;"></ul>
        </div>

    </div>
    <!--box end-->
</div>
<!--container end-->
<div class="footer">
    <div class="container">
        Copyright © 2016 kaishengit
    </div>
</div>
<script src="/static/js/jquery-1.11.1.js"></script>
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
            href: '?p={{number}}&nodeid=${param.nodeid}'
        });
    });
</script>
</body>
</html>
