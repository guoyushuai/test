<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>我的历史流程</title>

    <%@include file="../../include/css.jsp"%>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper" id="app">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="process_finished"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">


            <div class="box" id="his">
                <div class="box-header">
                    <h3 class="box-title">我的历史发起流程</h3>
                </div>
                <div class="box-body">
                    <table width="100%" class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>流程实例ID</th>
                            <th>所属流程</th>
                            <th>启动时间</th>
                            <th>流程启动人</th>
                            <th>结束时间</th>

                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach items="${processList}" var="process">

                            <tr>
                                <th><a href="/process/view/${process.processInstanceId}">${process.processInstanceId}</a></th>
                                <th>${process.processDefinitionName}</th>
                                <th>${process.applyTime}</th>
                                <th>${process.userName}</th>
                                <th>${process.updateTime}</th>
                            </tr>

                        </c:forEach>

                        </tbody>
                    </table>
                </div>
            </div>

            <div class="box" id="hisIn">
                <div class="box-header">
                    <h3 class="box-title">历史参与的流程</h3>
                </div>
                <div class="box-body">
                    <table width="100%" class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>流程实例ID</th>
                            <th>所属流程</th>
                            <th>启动时间</th>
                            <th>流程启动人</th>
                            <th>结束时间</th>
                            <th>状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${processTaskList}" var="process">
                            <tr>
                                <th><a href="/process/view/${process.processInstanceId}">${process.processInstanceId}</a></th>
                                <th>${process.processDefinitionName}</th>
                                <th>${process.applyTime}</th>
                                <th>${process.userName}</th>
                                <th>${process.updateTime}</th>
                                <th>${process.updateTime == null ? "未结束" : "已结束"}</th>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>


        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>

</body>

</html>