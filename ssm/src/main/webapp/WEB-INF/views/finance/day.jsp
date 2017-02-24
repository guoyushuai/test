<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>财务报表-日报</title>
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

    <jsp:include page="../include/sidebar.jsp">
        <jsp:param name="menu" value="finance_day"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>

                <small></small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 财务报表</a></li>
                <li class="active"><a href="#">日报</a></li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <%--根据日期筛选数据--%>
            <div class="box">
                <div class="box-body">
                    <form class="form-inline pull-left">
                        <input type="text" class="form-control" id="date">
                    </form>
                    <div class="box-tools pull-right">
                        <%--不能在这里直接写地址进行跳转，需要导出的是指定日期的数据，需要通过事件获得日期--%>
                        <%--<a href="/finance/export/day/$('#date').val()/data.xls"></a>//这里获取不到date框的值--%>
                        <a href="javascript:;" class="btn btn-default" id="exportXls"><i class="fa fa-file-excel-o"></i> 导出Excel</a>
                    </div>
                </div>
            </div>

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">财务日报</h3>

                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                            <i class="fa fa-minus"></i></button>
                        <button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
                            <i class="fa fa-times"></i></button>
                    </div>
                </div>
                <div class="box-body">

                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>id</th>
                            <th>财务流水号</th>
                            <th>创建日期</th>
                            <th>类型</th>
                            <th>金额</th>
                            <th>业务模块</th>
                            <th>业务流水号</th>
                            <th>状态</th>
                            <th>备注</th>
                            <th>#</th>
                        </tr>
                        </thead>

                        <tbody></tbody>

                    </table>

                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

            <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
            <%--Admin页面数据存在于各种盒子中--%>
            <div class="box">

                <%--标题(盒子头)--%>
                <div class="box-header with-border">
                    <h3 class="box-title">当日财务收支统计</h3>
                </div>

                <%--内容(盒子身)--%>
                <div class="box-body">

                    <div class="<%--form-group--%> row">
                        <div class="col-lg-6">
                            <div id="in" style="padding-left: 20px;width: 600px;height:400px;"></div>
                        </div>
                        <div class="col-lg-6">
                            <div id="out" style="padding-left: 20px;width: 600px;height:400px;"></div>
                        </div>
                    </div>

                </div>

            </div>

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

    $(function () {

        $("#date").val(moment().format("YYYY-MM-DD"));//默认当天，展示当天的数据

        $("#date").datepicker({
            format: "yyyy-mm-dd",
            language: "zh-CN",
            autoclose: true,
            endDate:moment().format("YYYY-MM-DD")
        }).on("changeDate",function (e) {//datepicker中添加事件
            var date = e.format(0,"yyyy-mm-dd");//修改input框中以format中的格式显示的日期为选择的日期

            //table 是DataTable的对象
            table.ajax.reload();//方法中有两个参数（false,null）;重新加载当前页，不会返回首页
            //这里修改日期后，需要重新返回第一页开始浏览当然天的数据（需要借助DataTable插件向服务端发出异步请求）

        });

        //返回一个对象
        var table = $(".table").DataTable({
            //定义每页显示的数量
            /*"lengthMenu": [5,10,50],*/
            "pageLength": 25,
            "lengthChange": false,
            //所有的操作都在服务端进行
            "serverSide": true,
            /*//服务端url地址
             "ajax":"/setting/device/load",*/
            //对象
            "ajax":{
                "url":"/finance/day/load",//url
                "type":"get",//方式
                "data":function(obj){//发送到服务端的数据，所有的(自行扩展的)键值对
                    obj.date = $("#date").val();//获取input框中的日期,传到服务端进行筛选查询
                }
            },
            //指定默认排序方式,哪一列的什么形式
            "order":[[0,'desc']],
            "ordering": false,//关闭其他列的排序功能
            //禁止使用自带的搜索
            "searching":false,
            //配置(响应)返回的JSON对象中的"data":[{}]对象数组中的属性中数据key和datatable表格中列序号的对应关系
            // (第1列对应id,第2列对应name,第3列对应unit ...),name属性值主要用于告诉服务端以哪一列进行排序
            "columns":[
                {"data":"id","name":"id"},
                {"data":"serialNumber"},
                {"data":"createDate"},
                {"data":"type"},
                {"data":"money"},
                {"data":"module"},
                {"data":"moduleSerialNumber"},
                {"data":"state"},
                {"data":"remark"},
                {"data":function(row){
                    //(row)obj当前行在data[]数组中的那个对象
                    if(row.state == "未确认") {
                        return "<a href='javascript:;' rel='"+row.id+"' class='confirmBtn'>确认</a>";
                    } else {
                        return "";
                    }
                }}

            ],
            //定义列的特征
            "columnDefs":[
                //要求必须有一个排序列，visible是否显示（id列不连续，除了用来排序没什么用，不显示）
                {targets:[0],visible: false},
                /*{targets:[1,2,6],orderable:false}*/
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

        /*表格中的确认按钮*//*事件委托*/
        $(document).delegate(".confirmBtn","click",function () {
            var id = $(this).attr("rel");//input通过$(this).val();取值
            $.post("/finance/confirm/" + id).done(function (result) {
                if(result.status == "success") {

                    table.ajax.reload(null,false);//刷新当前页面，防止自动跳转到默认的第一页

                } else {
                    layer.msg(result.message);
                }
            }).error(function () {
                layer.msg("服务器忙，请稍后再试！");
            });
        })

        /*导出Excel*/
        <%--所有的导出全部是文件下载，所有的导入全部文件上传--%>
        $("#exportXls").click(function () {
            var day = $("#date").val();//日期框中选中的日期
            /*$.get("/finance/export/day/"+day+"/data.xls");*/
            window.location.href = "/finance/export/day/"+day+"/data.xls";//跳转地址get请求，相当于下载页面
        });

    });


    // 基于准备好的dom，初始化echarts实例
    var inChart = echarts.init(document.getElementById('in'));
    option = {
        title : {
            text: '当日收入饼状图',
            subtext: '只统计收入',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['设备租赁','劳务外包','金融借贷','其他']
        },
        series : [
            {
                name: '收入',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:33500, name:'设备租赁'},
                    {value:12340, name:'劳务外包'},
                    {value:23400, name:'金融借贷'},
                    {value:13500, name:'其他'}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    inChart.setOption(option);


    // 基于准备好的dom，初始化echarts实例
    var outChart = echarts.init(document.getElementById('out'));
    outoption = {
        title : {
            text: '当日支出饼状图',
            subtext: '只统计支出',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['工资','日常开销','设备维修','设备更新','其他']
        },
        series : [
            {
                name: '支出',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:0, name:'工资'},
                    {value:400, name:'日常开销'},
                    {value:23400, name:'设备维修'},
                    {value:35000, name:'设备更新'},
                    {value:3500, name:'其他'}
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    outChart.setOption(outoption);
</script>
</body>
</html>
