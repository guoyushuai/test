<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>设备租赁</title>
    <%@include file="../../include/css.jsp"%>
    <%--<link rel="stylesheet" href="/static/plugins/datatables/dataTables.bootstrap.css">--%>
    <link rel="stylesheet" href="/static/plugins/datatables/jquery.dataTables.min.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../../include/header.jsp"%>
    <jsp:include page="../../include/sidebar.jsp">
        <jsp:param name="menu" value="business_device_rent"/>
    </jsp:include>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box box-primary box-solid">
                <div class="box-header with-border">
                    <h3 class="box-title">租赁合同列表</h3>

                    <div class="box-tools pull-right">
                        <a href="/device/rent/new" class="btn btn-primary"><i class="fa fa-plus"></i></a>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>流水号</th>
                            <th>租赁公司</th>
                            <th>电话</th>
                            <th>租赁日期</th>
                            <th>归还日期</th>
                            <th>租金</th>
                            <th>状态</th>
                            <th>#</th>
                        </tr>
                        </thead>
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

<%@include file="../../include/js.jsp"%>
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script>
    $(function () {
        /*var json = {
            A:"2233",
            B:"2233",
            C:"2233",
        };
        console.log(json);
        var str = JSON.stringify(json);
        console.log(str);
        var obj = JSON.parse(str);
        console.log(obj)*/

        /*/!*零配置,老版本dataTable*!/
         $(".table").DataTable();*/

        //返回一个对象
        var table = $(".table").DataTable({
            //每页显示几条数据不能修改
            "lengthChange":false,
            /*!//定义每页显示的数量
            "lengthMenu": [ 5,10, 25, 50, 75, 100 ],*/
            //更改初始页面长度（每页显示数量）固定
            "pageLength":25,
            //所有的操作都在服务端进行
            "serverSide": true,
            //对象
            "ajax":{
                "url":"/device/rent/load",//url
                "type":"get"//方式
                /*"data":function(obj){//发送到服务端的数据，所有的(自行扩展的)键值对
                    obj.deviceName = $("#q_device_name").val();
                }*/
            },
            //指定默认排序方式,哪一列的什么形式
            "order":[[0,'desc']],
            //不使用插件的排序
            "ordering":false,
            //禁止使用自带的搜索
            "searching":false,
            //根据页面宽度自动调整表格宽度
            "autoWidth":false,
            //配置(响应)返回的JSON对象中的"data":[{}]对象数组中的属性中数据key和datatable表格中列序号的对应关系
            // (第1列对应id,第2列对应name,第3列对应unit ...),name属性值主要用于告诉服务端以哪一列进行排序
            "columns":[
                {"data":"id","name":"id"},
                /*{"data":"serialNumber"},*///除了显示增加点击跳转到详情页功能
                {"data":function (row) {//row当前行的对象
                    if(row.serialNumber) {//有数据显示，没数据显示NUll,
                        return "<a href='/device/rent/" + row.serialNumber + "'>" + row.serialNumber + "</a>";
                    } else {
                        return "";//返回空字符串，避免显示为null
                    }
                }},
                {"data":"companyName"},
                {"data":"tel"},
                {"data":"rentDate"},
                {"data":"backDate"},
                {"data":"totalPrice"},
                {"data":"state"},//租赁状态
                {"data":function(row){//(row)obj当前行在data[]数组中的那个对象
                    if(row.state) {//state没有默认值，只要有值就不再显示该按钮选项
                        return "";
                    } else {
                        return "<a href='javascript:;' rel='" + row.id + "' class='btn btn-xs btn-default checkBtn'> <i class='fa fa-check-circle-o'></i> 完成 </a> ";
                    }
                }},

            ],
            //定义列的特征
            "columnDefs":[
                //要求必须有一个排序列，visible是否显示（id列不连续，除了用来排序没什么用，不显示）
                //orderable:false,不做排序
                {targets:[0],visible: false},
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

        $(document).delegate(".checkBtn","click",function(){
            var id = $(this).attr("rel");
            layer.confirm("确定合同已完成么？",{btn:['确定','取消']},function(index){
                /*var id = $(this).attr("rel");//原生的弹框，$(this)才能获取到值。*/
               $.post("/device/rent/state/change",{"id":id}).done(function (result) {
                    if(result == "success") {//没有默认值 if(result.state)也可以，只要有值就为true

                        table.ajax.reload();//刷新表格datatable中的方法

                    }
               }).error(function () {
                    layer.msg("服务器忙，请稍后再试！");
               });
               layer.close(index);//关闭弹窗用
            });
        });

        /*绑定事件时，元素不存在，数据都是后来动态添加上去的，删除事件绑定不上*/
        /*$(".delLink").click(function () {
         if(confirm("确定要删除吗?")) {
         }
         });*/

        /*/!*使用事件委托机制*!/数据是进入页面后通过dataTables插件中的ajax请求添加上来的,原生的事件直接绑定不上*/
        /*$(document).delegate(".delLink","click",function(){
            if(confirm("确定要删除吗?")) {
                var id = $(this).attr("rel");//原生的弹框，$(this)能获取到值。
                $.get("/setting/device/"+id+"/del")
                    .done(function(data){
                        if(data == "success") {
                            alert("删除成功");

                            //dataTables返回的对象table重新加载,刷新box,不是整个页面刷新
                            table.ajax.reload();
                        }
                    })
                    .error(function(){
                        alert("服务器异常");
                    });
            }
        });*/

    })
</script>
</body>
</html>
