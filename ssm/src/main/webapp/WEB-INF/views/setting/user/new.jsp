<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Dashboard</title>
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
                <small></small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i>系统设置</a></li>
                <li class="active">添加用户</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">添加用户</h3>

                    <%--<div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                            <i class="fa fa-minus"></i></button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
                            <i class="fa fa-times"></i></button>
                    </div>--%>
                </div>
                <div class="box-body">
                    <form method="post">
                        <div class="from-group">
                            <label>账号</label>
                            <input type="text" name="username" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>密码</label>
                            <input type="password" name="password" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>角色</label>
                            <div class="form-control">
                                <c:forEach items="${roleList}" var="role">
                                    <%--横板复选框；复选框的name属性值设置相同,请求头form data发送数组--%>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" name="roleids" value="${role.id}"> ${role.viewName}
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="from-group">
                            <button class="btn btn-success">保存</button>
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
