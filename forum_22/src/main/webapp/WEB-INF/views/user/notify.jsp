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
                    <%--避免破坏表结构--%>
                    <c:if test="${not empty requestScope.notifyList}">
                        <c:forEach items="${requestScope.notifyList}" var="notify">
                            <span class="exitunread" hidden><c:if test="${notify.state == 0}">exitunread</c:if></span>
                        </c:forEach>
                    </c:if>
                        <input type="checkbox" id="ckFather">
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

                        <c:choose>
                            <c:when test="${notify.state == 0}">
                                <tr>
                                    <td><input value="${notify.id}" type="checkbox" class="ckSon"></td>
                                    <td>${notify.createtime}</td>
                                        <%--<td>您发布的主题<a>${topic.title}</a>有了新的回复</td>--%>
                                        <%--将通知需要的内容封装好，再保存进数据库，可以少做一次topic的查询--%>
                                    <td>${notify.content}</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr style="text-decoration: line-through">
                                    <td></td>
                                    <td>${notify.createtime}</td>
                                        <%--<td>您发布的主题<a>${topic.title}</a>有了新的回复</td>--%>
                                        <%--将通知需要的内容封装好，再保存进数据库，可以少做一次topic的查询--%>
                                    <td>${notify.content}</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>

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

    $(function () {

        var exitunread = $(".exitunread").text();
        if(exitunread == "") {
            //class="hide"也行
            $("#ckFather").attr("hidden","hidden");
        };

        $("#markBtn").click(function () {
            var ids = [];
            var $ckSons = $(".ckSon");
            for(var i = 0;i < $ckSons.length;i++) {
                if($ckSons[i].checked == true) {
                    /*原生javascript->value没有小括号（）,jQuery->val()*/
                    ids.push($ckSons[i].value);
                }
            }

            /*$.post 的格式有多种，都可以*/
            $.post("/notifyRead",{"ids":ids.join(",")},function (result) {
                if(result == "success") {
                    /*等同于刷新*/
                    window.history.go(0);
                }
            })
        });
        

        //jquery对象没有原生的javascript对象的checked属性
        //父选框点击事件
        $("#ckFather").click(function () {
            var $ckSons = $(".ckSon");
            for(var i = 0 ;i < $ckSons.length;i++) {
                $ckSons[i].checked = $("#ckFather")[0].checked;
            }

            //标记为已读按钮的可点击状态
            if($("#ckFather")[0].checked == true) {
                $("#markBtn").removeAttr("disabled");
            } else {
                $("#markBtn").attr("disabled","disabled");
            }
        });


        //子选框点击事件
        $(".ckSon").click(function () {
            var $ckSons = $(".ckSon");
            var checkedNum = 0;
            for(var i = 0;i < $ckSons.length;i++) {
                if($ckSons[i].checked == true) {
                    checkedNum++;
                }
            }
            alert(checkedNum);
            if(checkedNum == $ckSons.length) {
                $("#ckFather")[0].checked = true;
            } else {
                $("#ckFather")[0].checked = false;
            }

            //标记为已读按钮的可点击状态
            if(checkedNum > 0) {
                $("#markBtn").removeAttr("disabled");
            } else {
                $("#markBtn").attr("disabled","disabled");
            }
        });

    });

</script>
</body>
</html>
