<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Dashboard</title>
    <%@ include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/datatables/jquery.dataTables.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="../../include/header.jsp"%>

    <%--<%@ include file="../include/sidebar.jsp"%>--%>
    <%--当前节点高亮显示--%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="sys_device"/>
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
                <li class="active">设备管理</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->

            <div class="box box-solid box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title"><i class="fa fa-search"></i> 搜索</h3>
                </div>
                <div class="box-body">
                    <%--以Get方式提交到当前url中--%>
                    <form class="form-inline">
                        <div class="form-group">
                            <input class="form-control" type="text" name="q_name" value="${queryName}" placeholder="姓名" >
                        </div>
                        <div class="form-group">
                            <select class="form-control" name="q_role" >
                                <option value="">--角色--</option>
                                <c:forEach items="${roleList}" var="role">
                                    <option value="${role.id}" ${role.id == queryRole ? 'selected' : ''}>${role.viewName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button class="btn btn-default">搜索</button>
                    </form>
                </div>
            </div>

            <div class="box">

                <div class="box-header with-border">
                    <h3 class="box-title">设备管理</h3>

                    <div class="box-tools pull-right">
                        <a href="/setting/device/new" class="btn"><i class="fa fa-plus"></i></a>
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
                        <th>id</th>
                        <th>名称</th>
                        <th>单位</th>
                        <th>总数</th>
                        <th>库存</th>
                        <th>价格</th>
                        <th width="100">#</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%--页面显示数据通过插件ajax方式查询--%>
                    <%--<c:forEach items="${deviceList}" var="device">
                        <tr>
                            <td>${device.name}</td>
                            <td>${device.unit}</td>
                            <td>${device.totalNum}</td>
                            <td>${device.currentNum}</td>
                            <td>${device.price}</td>
                            <td>
                                <a href="">##</a>
                            </td>
                        </tr>
                    </c:forEach>--%>

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

    <%--<%@ include file="include/controlsidebar.jsp"%>--%>

</div>
<!-- ./wrapper -->
<%@ include file="../../include/js.jsp"%>
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script>
    $(function () {
        /*/!*零配置,老版本dataTable*!/
        $(".table").DataTable();*/

        $(".table").DataTable({
            //定义每页显示的数量
            "lengthMenu": [ 5,10, 25, 50, 75, 100 ],
            //所有的操作都在服务端进行
            "serverSide": true,
            //服务端url地址
            "ajax":"/setting/device/load",
            /*//指定默认排序方式
            "order":[0,'desc'],
            //禁止使用自带的搜索
            "searching":false,*/
            //配置返回的JSON中[data]属性中数据key和表格列的对应关系
            "columns":[
                {"data":"id"},
                {"data":"name"},
                {"data":"unit"},
                {"data":"totalNum"},
                {"data":"currentNum"},
                {"data":"price"},
                {"data":function(){
                    return "";
                }}

            ],
            //定义列的特征
            "columnDefs":[
                //要求必须有一个排序列，visible是否显示（id列不连续，除了用来排序没什么用，不显示）
                {targets:[0],visible: false},
                {targets:[1,2,3,4,5],orderable:false}
            ],
            //定义中文
            "language":{
                "search": "搜索:",
                "zeroRecords":    "没有匹配的数据",
                "lengthMenu":     "显示 _MENU_ 条数据",
                "info":           "显示从 _START_ 到 _END_ 条数据 共 _TOTAL_ 条数据",
                "infoFiltered":   "(从 _MAX_ 条数据中过滤得来)",
                "loadingRecords": "加载中...",
                "processing":     "处理中...",
                "paginate": {
                    "first":      "首页",
                    "last":       "末页",
                    "next":       "下一页",
                    "previous":   "上一页"
                }
            }
        });

    });
</script>
</body>
</html>

