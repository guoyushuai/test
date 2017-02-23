<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>编辑用户</title>
    <%@ include file="../../include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="../../include/header.jsp"%>

    <%@ include file="../../include/sidebar.jsp"%>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                系统设置
                <small>Control panel</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i>系统设置</a></li>
                <li class="active">编辑用户</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">编辑用户</h3>

                    <%--<div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                            <i class="fa fa-minus"></i></button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
                            <i class="fa fa-times"></i></button>
                    </div>--%>
                </div>
                <div class="box-body">
                    <form method="post">
                        <%--提交修改时用id属性指定修改的是哪一个用户--%>
                        <input type="hidden" name="id" value="${user.id}">
                        <div class="from-group">
                            <label>账号</label>
                            <input type="text" name="username" value="${user.username}" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>密码</label>
                            <input type="password" name="password" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>角色</label>
                            <div>
                                <%--所有的角色集合--%>
                                <c:forEach items="${roleList}" var="role">
                                    <c:set var="flag" value="false" scope="page"/>
                                    <%--用户拥有的角色集合--%>
                                    <c:forEach items="${user.roleList}" var="userRole">
                                        <c:if test="${role.id == userRole.id}">
                                            <label class="checkbox-inline">
                                                <input type="checkbox" checked name="roleids" value="${role.id}"> ${role.viewName}
                                            </label>
                                            <c:set var="flag" value="true"/>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${not flag}">
                                        <label class="checkbox-inline">
                                            <input type="checkbox" name="roleids" value="${role.id}"> ${role.viewName}
                                        </label>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="from-group">
                            <button class="btn btn-success">修改</button>
                        </div>
                    </form>
                </div>
                <!-- /.box-body -->

            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <%--<footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 2.3.3
        </div>
        <strong>Copyright &copy; 2014-2015 <a href="http://almsaeedstudio.com">Almsaeed Studio</a>.</strong> All rights
        reserved.
    </footer>--%>

    <%--<%@ include file="include/controlsidebar.jsp"%>--%>

</div>
<!-- ./wrapper -->
<%@ include file="../../include/js.jsp"%>
</body>
</html>

