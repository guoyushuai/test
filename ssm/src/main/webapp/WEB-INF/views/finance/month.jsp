<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>财务报表-月报</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@ include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/datatables/jquery.dataTables.min.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@ include file="../include/header.jsp"%>

    <!-- =============================================== -->

    <!-- Left side column. contains the sidebar -->

    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="finance_month"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>

                <small></small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 财务报表</a></li>
                <li class="active">月报</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">财务月报</h3>

                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                            <i class="fa fa-minus"></i></button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
                            <i class="fa fa-times"></i></button>
                    </div>
                </div>
                <div class="box-body">
                    <div class="box">

                        <div class="box-body">
                            <table class="table table-bordered">
                                <tr>
                                    <th>日期</th>
                                    <th>当日收入</th>
                                    <th>当日支出</th>
                                    <th>备注</th>
                                    <th>操作</th>

                                </tr>
                                <tr>
                                    <td>2016-10-1</td>
                                    <td>15000</td>
                                    <td>2000</td>
                                    <td>无</td>
                                    <td><a href="dayReport.html">详情</a></td>
                                </tr>
                                <tr>
                                    <td>2016-10-2</td>
                                    <td>15000</td>
                                    <td>2000</td>
                                    <td>无</td>
                                    <td><a href="dayReport.html">详情</a></td>
                                </tr>
                                <tr>
                                    <td>2016-10-3</td>
                                    <td>15000</td>
                                    <td>2000</td>
                                    <td>无</td>
                                    <td><a href="dayReport.html">详情</a></td>
                                </tr>
                                <tr>
                                    <td>2016-10-4</td>
                                    <td>15000</td>
                                    <td>2000</td>
                                    <td>无</td>
                                    <td><a href="dayReport.html">详情</a></td>
                                </tr>
                            </table>
                        </div>
                        <!-- /.box-body -->
                        <div class="box-footer clearfix">
                            <ul class="pagination pagination-sm no-margin pull-right">
                                <li><a href="#">&laquo;</a></li>
                                <li><a href="#">1</a></li>
                                <li><a href="#">2</a></li>
                                <li><a href="#">3</a></li>
                                <li><a href="#">&raquo;</a></li>
                            </ul>
                        </div>
                    </div>
                    <!-- /.box -->
                    <div style="font-size:18px;font-weight:100" >2016年10月：收入<span class="alert-success">100000.00</span>元，支出<span class="alert-error">50000.00</span>元，盈利<span class="alert-warning">50000.00</span>元</div>
                </div>
                <!-- /.box-body -->

            </div>
            <!-- /.box -->
            <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
            <div id="month" style="width:auto;height:800px"></div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>
<!-- ./wrapper -->

<%@ include file="../include/js.jsp"%>
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/moment.js"></script>
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/echarts.min.js"></script>

<script>

    // 基于准备好的dom，初始化echarts实例
    var monthChart = echarts.init(document.getElementById('month'));


    option = {
        title : {
            text: '10月收入支出统计图',
            subtext: ''
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['收入','支出']
        },
        toolbox: {
            show : true,
            feature : {
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['1','2','3','4','5','6','8','9','10','11','12','13','14','15','16','18','19','20','21','22','26','27']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'收入',
                type:'bar',
                data:[2230, 4119, 7110, 2312, 2516, 7617, 1356, 1622, 326, 2000, 6400, 3030,2230, 4119, 7110, 2312, 2516, 7617, 1356, 1622, 326, 2000, ],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                }
            },
            {
                name:'支出',
                type:'bar',
                data:[2600, 5900, 9000, 2640, 2870, 7017, 1756, 1822, 4870, 1880, 2600, 5900, 9000, 2640, 2870, 7017, 1756, 1822, 4870, 1880, 600, 230],
                markPoint : {
                    data : [
                        {name : '年最高', value : 9000, xAxis: 2, yAxis: 9000},
                        {name : '年最低', value : 230, xAxis: 21, yAxis: 230}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            }
        ]
    };


    // 使用刚指定的配置项和数据显示图表。
    monthChart.setOption(option);
</script>
</body>
</html>
