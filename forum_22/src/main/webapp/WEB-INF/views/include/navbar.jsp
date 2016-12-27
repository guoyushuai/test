<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="header-bar">
    <div class="container">
        <a href="/home" class="brand">
            <i class="fa fa-reddit-alien"></i>
        </a>
        <ul class="unstyled inline pull-right">
            <c:choose>
                <c:when test="${not empty sessionScope.current_user}">
                    <li>
                        <a href="/setting">
                            <img id="navbar_avatar" src="http://oi0nv2rk1.bkt.clouddn.com/${sessionScope.current_user.avatar}?imageView2/1/w/20/h/20" class="img-circle" alt="">
                        </a>
                    </li>
                    <li>
                        <a href="/newTopic"><i class="fa fa-plus"></i></a>
                    </li>
                    <li>
                        <a href="/notify"><i class="fa fa-bell"><span id="unreadnum" class="badge"></span></i></a>
                    </li>
                    <li>
                        <a href="/setting"><i class="fa fa-cog"></i></a>
                    </li>
                    <li>
                        <a href="/logout"><i class="fa fa-sign-out"></i></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <a href="/login"><i class="fa fa-sign-in"></i></a>
                    </li>
                </c:otherwise>
            </c:choose>

            <span hidden id="islogin"><c:if test="${not empty sessionScope}">login</c:if></span>

        </ul>
    </div>
</div>
<!--header-bar end-->

<script src="/static/js/jquery-1.11.1.js"></script>
<%--<script src="/static/js/user/notify.js"></script>--%>
<script>
    $(function () {

        /*保持消息通知的实时性，轮询，间歇调用*/
        var loadNotify = function () {
            /*NotifyServlet已有get方法*/
            $.post("/notify").done(function (result) {
                if(result.state == "success") {
                    if(result.data == "0") {
                        $("#unreadnum").text("");
                    } else {
                        $("#unreadnum").text(result.data);
                    }
                }
            });
        };
        loadNotify();//运行

        //防止没有登录时，依然在无限轮询，被过滤器过滤后又会返回登陆页的HTML
        var login = $("#islogin").text();
        if(login == "login") {
            setInterval(loadNotify,3*1000);
        }

    });
</script>