<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>流程信息--${process.processInstanceId}</title>

    <%@include file="../../include/css.jsp"%>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->

<div class="wrapper" id="app">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="process_view"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">

            <!-- Main content -->
            <section class="content">

                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">已归档流程实例信息<a class="btn" href="#" onclick="history.go(-1);">返回列表</a></h3>
                    </div>
                    <div class="box-body">
                        <legend>流程综合信息-${process.processDefinitionName}-${process.processInstanceId}</legend>
                        <table class="table table-bordered table-hover table-condensed">
                            <tbody><tr>
                                <th width="100">流程ID</th>
                                <td>${process.processInstanceId}</td>
                                <th width="100">流程名称</th>
                                <td>${process.processDefinitionName}</td>
                                <th width="100">业务KEY</th>
                                <td>${process.historicProcessInstance.businessKey}</td>
                            </tr>
                            <tr>
                                <th width="100">流程启动时间</th>
                                <td>${process.applyTime}</td>
                                <th width="100">流程结束时间</th>
                                <td>${process.updateTime}</td>
                                <th width="100">流程状态</th>
                                <c:choose>
                                    <c:when test="${not empty process.updateTime}">
                                        <td>已结束</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>未结束</td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                            </tbody>
                        </table>

                        <fieldset style="margin-top: 20px">
                            <legend>活动记录</legend>
                            <table width="100%" class="table table-bordered table-hover table-condensed">
                                <thead>
                                <tr>
                                    <th>活动ID</th>
                                    <th>活动名称</th>
                                    <th>活动类型</th>
                                    <th>任务ID</th>
                                    <th>办理人</th>
                                    <th>活动开始时间</th>
                                    <td>活动结束时间</td>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach items="${process.hisActList}" var="hisAct">
                                    <tr>
                                        <td>${hisAct.activityId}</td>
                                        <td>${hisAct.activityName}</td>
                                        <td>${hisAct.activityType}</td>
                                        <td>${hisAct.taskId}</td>
                                        <td>${hisAct.assignee}</td>
                                        <td><fmt:formatDate value="${hisAct.startTime}" pattern="yyyy-MM-dd hh:mm"/></td>
                                        <td><fmt:formatDate value="${hisAct.endTime}" pattern="yyyy-MM-dd hh:mm"/></td>
                                    </tr>
                                </c:forEach>

                                </tbody>
                            </table>
                        </fieldset>


                        <div>
                            <%--根据部署id查找到存储的资源--%>
                            <img id="processDiagram" src="/process/resource/${process.processDefinition.deploymentId}?resourceName=${process.processDefinition.diagramResourceName}">
                        </div>
                    </div>
                </div>
            </section>
            <!-- /.content -->
        </div>
        <!-- /.content-wrapper -->
</div>

<%@include file="../../include/js.jsp"%>

</body>

</html>