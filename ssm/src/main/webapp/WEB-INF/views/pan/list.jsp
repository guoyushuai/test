<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>网盘系统</title>
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/webuploader/webuploader.css">
    <style>
        #picker{
            float : left;
            margin-right : 20px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>
    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="pan"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <%--<section class="content-header">--%>
        <%--<h1>--%>
        <%--Blank page--%>
        <%--<small>it all starts here</small>--%>
        <%--</h1>--%>
        <%--<ol class="breadcrumb">--%>
        <%--<li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>--%>
        <%--<li><a href="#">Examples</a></li>--%>
        <%--<li class="active">Blank page</li>--%>
        <%--</ol>--%>
        <%--</section>--%>

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">网盘系统</h3>

                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                            <i class="fa fa-minus"></i></button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
                            <i class="fa fa-times"></i></button>
                    </div>
                </div>
                <div class="box-body">
                    <div id="picker">上传文件</div>
                    <button class="btn btn-primary" id="newfolder">新建文件夹</button>
                    <c:if test="${not empty fid and fid != 0}">
                        <button class="btn btn-primary pull-right" id="back">返回上级</button>
                    </c:if>
                    <table class="table">
                        <thead>
                        <tr>
                            <th></th>
                            <th>名称</th>
                            <th>大小</th>
                            <th>创建时间</th>
                            <th>创建人</th>
                            <th>#</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty diskList}">
                            <tr>
                                <td colspan="6">暂无资源</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${diskList}" var="disk">
                            <tr>
                                <td>
                                        <%--文件文件夹的图标不同--%>
                                    <c:choose>
                                        <c:when test="${disk.type == 'file'}">
                                            <i class="fa fa-file"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fa fa-folder"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                        <%--文件点击下载，文件夹点击进入下级目录--%>
                                    <c:choose>
                                        <c:when test="${disk.type == 'file'}">
                                            <a href="/path/download?id=${disk.id}">${disk.sourceName}</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/pan?path=${disk.id}">${disk.sourceName}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${disk.size}</td>
                                <td>${disk.createTime}</td>
                                <td>${disk.createUser}</td>
                                <td></td>
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

</div>

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/webuploader/webuploader.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script>
    $(function () {

        var uploader = WebUploader.create({
            swf : "/static/plugins/uploader/Uploader.swf",
            server : "/pan/upload",//上传文件post提交
            pick : "#picker",
            auto : true,
            fileVal : "file",
            formData : {"fid":${fid}}//向客户端传值
        });

        uploader.on( 'uploadSuccess', function( file ,result ) {
            /*alert(result.status);
            alert(result.data);*/
            //AjaxResult构造方法，status（String）与data（object） 包含
            if(result.status == "success") {
                lay.msg("上传成功");
                window.history.go(0);
            } else {
                layer.msg(result.message);
            }
        });

        uploader.on( 'uploadError', function( file ) {
            layer.msg("服务器忙，请稍后再试！");
        });


        $("#newfolder").click(function () {
            var fid = ${fid};//新建文件夹时，从服务端传入（从客户端url中获取不靠谱），在目前显示的目录下创建
            layer.prompt({title:"请输入文件夹名称"},function (text,index) {//title:弹框的标题；text:弹框中输入的内容；index:用于关闭该弹框
                layer.close(index);

                $.post("/pan/folder/new",{"fid":fid,"sourceName":text}).done(function (result) {
                    if(result == "success") {

                        window.history.go(0);//刷新页面

                    }
                }).error(function () {
                    layer.msg("服务器忙,请稍后再试!");
                });
            });
        });

        $("#back").click(function () {
            window.history.go(-1);
        });


    });
</script>
</body>
</html>
