<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>调整申请</title>

    <%@include file="../../include/css.jsp"%>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper" id="app">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="process_list"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">调整申请</h3>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-5">
                            <form action="/leave/task/modify/${leave.task.id}" class="form-horizontal" method="post" >
                                <%--便于修改时根据id到t_leave表中查找到对应的leave对象--%>
                                <input type="hidden" name="id" value="${leave.id}" />
                                <fieldset>
                                    <div id="messageBox" class="alert alert-error input-large controls" style="display:none">输入有误，请先更正。</div>
                                    <div class="control-group">
                                        <label class="control-label">请假类型:</label>
                                        <div class="controls">
                                            <select id="leaveType" name="leaveType" class="required form-control">
                                                <option ${leave.leaveType == "公休" ? 'selected' : ''}>公休</option>
                                                <option ${leave.leaveType == "病假" ? 'selected' : ''}>病假</option>
                                                <option ${leave.leaveType == "调休" ? 'selected' : ''}>调休</option>
                                                <option ${leave.leaveType == "事假" ? 'selected' : ''}>事假</option>
                                                <option ${leave.leaveType == "婚假" ? 'selected' : ''}>婚假</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">开始时间:</label>
                                        <div class="controls">
                                            <input type="text" id="startTime" name="startTime" value="${leave.startTime}" readonly class="form_datetime input-small form-control">
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">结束时间:</label>
                                        <div class="controls">
                                            <input type="text" id="endTime" name="endTime" value="${leave.endTime}" readonly class="form_datetime input-small form-control">
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">请假原因:</label>
                                        <textarea name="reason" class="form-control">${leave.reason}</textarea>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">是否继续申请：</label>
                                        <%--input type="radio" 代表单选按钮--%>
                                        <input type="radio" name="reApply" value="true" checked/>重新申请
                                        <input type="radio" name="reApply" value="false"/>结束流程
                                    </div>
                                    <div class="control-group" style="padding-top: 15px">

                                        <button type="submit" class="btn btn-primary">完成任务</button>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>
<%@include file="../../include/js.jsp"%>

<script type="text/javascript" src="/static/plugins/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="/static/plugins/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript">
    $(function() {
        $(".form_datetime").datetimepicker({
            format: 'yyyy-mm-dd hh:ii',
            language:'zh-CN',
            autoclose: true,
            minuteStep: 30,
            startDate:"2017-03-06 10：00",
            //startDate: "2013-02-14 10:00"
        });
    });


</script>
</body>

</html>