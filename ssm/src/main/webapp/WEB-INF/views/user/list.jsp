<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Dashboard</title>
    <%@ include file="../include/css.jsp"%>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="../include/header.jsp"%>

    <%--<%@ include file="../include/sidebar.jsp"%>--%>
    <%--当前节点高亮显示--%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="sys_accounts"/>
    </jsp:include>

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
                <li class="active">账户管理</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">

                <div class="box-header with-border">
                    <h3 class="box-title">账户管理</h3>

                    <div class="box-tools pull-right">
                        <a href="/user/new" class="btn"><i class="fa fa-plus"></i></a>
                        <%--<button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                            <i class="fa fa-minus"></i></button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
                            <i class="fa fa-times"></i></button>--%>
                    </div>
                </div>

                <div class="box-body">
                    <%--requestscope--%>
                    <c:if test="${not empty message}">
                        <div class="alert alert-success">
                             <strong>${message}</strong>
                            <%--<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>--%>
                            <%--关闭提示框--%>
                             <button type="button" class="close" data-dismiss="alert"><i class="fa fa-times"></i></button>
                        </div>
                    </c:if>

                <table class="table">
                    <thead>
                    <tr>
                        <th>姓名</th>
                        <th>角色</th>
                        <th width="100">操作</th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach items="${userList}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.roleNames}</td>
                            <td>
                                    <%--注意url的设计--%>
                                <a href="/user/${user.id}/edit">编辑</a>
                                <a href="/user/${user.id}/del">删除</a>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
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
<%@ include file="../include/js.jsp"%>
</body>
</html>

