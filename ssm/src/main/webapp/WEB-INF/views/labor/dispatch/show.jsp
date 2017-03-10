<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>劳务派遣合同显示</title>
    <%@include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/webuploader/webuploader.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
    <link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper" id="app">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="business_labor_dispatch"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <%--打印时显示--%>
            <h3 style="text-align: center" class="visible-print-block">凯盛软件雇佣合同清单</h3>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">雇佣合同详情</h3>

                    <%--打印时隐藏--%>
                    <div class="box-tools pull-right hidden-print">
                        <button id="print" class="btn btn-default btn-sm"><i class="fa fa-print"></i>打印</button>
                    </div>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>公司名称</label>
                                ${dispatch.companyName}
                            </div>
                            <div class="form-group">
                                <label>联系电话</label>
                                ${dispatch.tel}
                            </div>
                            <div class="form-group">
                                <label>开始日期</label>
                                ${dispatch.dispatchDate}
                            </div>
                            <div class="form-group">
                                <label>总金额</label>
                                ${dispatch.totalPrice}
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>法人代表</label>
                                ${dispatch.linkMan}
                            </div>
                            <div class="form-group">
                                <label>地址</label>
                                ${dispatch.address}
                            </div>
                            <div class="form-group">
                                <label>结束日期</label>
                                ${dispatch.backDate}
                            </div>
                            <div class="form-group">
                                <label>预付款</label>
                                ${dispatch.preCost}
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>身份证号</label>
                                ${dispatch.cardNum}
                            </div>
                            <div class="form-group">
                                <label>传真</label>
                                ${dispatch.fax}
                            </div>
                            <div class="form-group">
                                <label>总天数</label>
                                ${dispatch.totalDays}
                            </div>
                            <div class="form-group">
                                <label>尾款</label>
                                ${dispatch.lastCost}
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">工种列表</h3>
                </div>
                <div class="box-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>工种名称</th>
                            <th>单位</th>
                            <th>雇佣单价</th>
                            <th>数量</th>
                            <th>总价</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${detailList}" var="detail">
                            <tr>

                                <td>${detail.laborName}</td>
                                <td>${detail.laborUnit}</td>
                                <td>${detail.laborPrice}</td>
                                <td>${detail.num}</td>
                                <td>${detail.totalPrice}</td>

                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <%--打印时隐藏--%>
            <div class="box hidden-print">
                <div class="box-header">
                    <h3 class="box-title">合同扫描件</h3>
                    <div class="box-tools pull-right">
                        <%--根据合同号，将同意合同上传的所有合同文件一起打包下载下来--%>
                        <%--btn-lg btn-sm btn-xs按钮大小--%>
                        <a href="/file/download/dispatch/doc/zip?id=${dispatch.id}" class="btn btn-sm btn-default">
                            <i class="fa fa-file-archive-o"></i> 打包下载
                        </a>
                    </div>
                </div>
                <div class="box-body">
                    <ul id="fileList">
                        <c:forEach items="${docList}" var="doc">
                            <%--进入页面内就加载的数据，不用做事件委托--%>
                            <li><a href="/file/download/dispatch/doc?id=${doc.id}">${doc.sourceName}</a></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <%--出门证,打印时显示--%>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->


</div>

<%@include file="../../include/js.jsp"%>
<script>
    $(function () {
        //打印页面
        $("#print").click(function () {
            window.print();
        });
    });
</script>
</body>
</html>
