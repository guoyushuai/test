<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>新增劳务派遣合同</title>
    <%@include file="../../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/webuploader/webuploader.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
    <link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
    <link rel="stylesheet" href="/static/plugins/webuploader/webuploader.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<%--将页面交给vue管理--%>
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
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">新增雇佣合同</h3>

                    <div class="box-tools pull-right">
                        <a href="/labor/dispatch" class="btn btn-default btn-sm"><i class="fa fa-reply"></i></a>
                    </div>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>公司名称</label>
                                <input type="text" class="form-control" id="companyName" tabindex="1">
                            </div>
                            <div class="form-group">
                                <label>联系电话</label>
                                <input type="text" class="form-control" id="tel" tabindex="4">
                            </div>
                            <div class="form-group">
                                <label>雇佣日期</label>
                                <%--默认今天，只读不能修改--%>
                                <%--关键时间点不能信赖客户端时间，读取服务器时间传过来比较好--%>
                                <input type="text" class="form-control" id="dispatchDate" readonly <%--tabindex="7"--%>>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>法人代表</label>
                                <input type="text" class="form-control" id="linkMan" tabindex="2">
                            </div>
                            <div class="form-group">
                                <label>地址</label>
                                <input type="text" class="form-control" id="address" tabindex="5">
                            </div>
                            <div class="form-group">
                                <label>结束日期</label>
                                <input type="text" class="form-control" id="backDate" tabindex="8">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>身份证号</label>
                                <input type="text" class="form-control" id="cardNum" tabindex="3">
                            </div>
                            <div class="form-group">
                                <label>传真</label>
                                <input type="text" class="form-control" id="fax" tabindex="6">
                            </div>
                            <div class="form-group">
                                <label>总天数</label>
                                <input type="text" class="form-control" id="totalDays" readonly <%--tabindex="9"--%> <%--v-model="days"--%>>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">工种选择</h3>
                    <div class="box-tools pull-right">
                        <%--页面内模态框--%>
                        <button class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal"><i class="fa fa-plus"></i></button>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>工种名称</th>
                            <th>单位</th>
                            <th>雇佣单价</th>
                            <th>人数</th>
                            <th>总价</th>
                            <td>#</td>
                        </tr>
                        </thead>
                        <tbody>
                        <%--vue.js的条件渲染功能v-if="laborArray.length == 0"--%>
                        <tr v-if="laborArray.length == 0">
                            <td colspan="6">暂无数据</td>
                        </tr>
                        <%--vue.js的循环功能v-for="labor in laborArray"--%>
                        <tr v-for="labor in laborArray">
                            <td>{{labor.name}}</td>
                            <td>{{labor.unit}}</td>
                            <td>{{labor.price}}</td>
                            <td>{{labor.num}}</td>
                            <td>{{labor.total}}</td>
                            <td><a href="javascript:;" v-on:click="removeLabor(labor)"><i class="fa fa-trash text-danger"></i></a></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="box-footer" style="text-align: right">
                    总雇佣费 {{total}} 元 预付款 {{preCost}} 元 尾款 {{lastCost}} 元
                </div>
            </div>

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">合同扫描件</h3>
                </div>
                <div class="box-body">
                    <div id="picker">选择文件</div><%--webuploader文件上传默认标签及选择器--%>
                    <p>注意：上传合同扫描件要求清晰可见 合同必须公司法人签字盖章</p>
                    <%--上传后在页面上显示原始名字--%>
                    <ul id="fileList">
                    </ul>
                    <button class="btn btn-primary pull-right" type="button" v-on:click="saveRent">保存合同</button>
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <%--modal(bootstrap的javascript(bootstrap.js)的模态框modal.js)--%>
    <div class="modal fade" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">选择工种</h4>
                </div>
                <div class="modal-body">
                    <form action="">
                        <div class="form-group">
                            <%--deviceName，添加后需要在租赁设备列表中显示--%>
                            <input type="hidden" id="laborName">
                            <label>工种名称</label>
                            <%--带查询功能的下拉框--%>
                            <select id="laborId" style="width: 300px;" class="form-control">
                                <%--下拉框未选择时的默认值--%>
                                <option value="">选择工种</option>

                                <%--<div v-for="device in ${deviceList}">//vue.js的循环不适用于下拉框的显示
                                    <option value="${device.id}">${device.name}</option>
                                </div>--%>

                                <%--laborService.findAllLabors();进入页面时查询的所有工种信息--%>
                                <c:forEach items="${laborList}" var="labor">
                                    <option value="${labor.id}">${labor.name}</option>
                                </c:forEach>
                                <%--laborService.findLaborById(id);下拉框选择后异步查询的该工种信息--%>

                            </select>
                        </div>
                        <div class="form-group">
                            <label>工人数量</label>
                            <input type="text" class="form-control" id="currNum" readonly>
                        </div>
                        <div class="form-group">
                            <label>单位</label>
                            <input type="text" class="form-control" id="unit" readonly>
                        </div>
                        <div class="form-group">
                            <label>雇佣单价</label>
                            <input type="text" class="form-control" id="dispatchPrice" readonly>
                        </div>
                        <div class="form-group">
                            <label>雇佣数量</label>
                            <input type="text" class="form-control" id="dispatchNum">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" v-on:click="addLabor">加入列表</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>

<%@include file="../../include/js.jsp"%>
<script src="/static/plugins/webuploader/webuploader.min.js"></script>
<%--momentjs--%>
<script src="/static/plugins/moment.js"></script>
<%--datepicker--%>
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<%--select2--%>
<script src="/static/plugins/select2/select2.full.min.js"></script>
<%--layer--%>
<script src="/static/plugins/layer/layer.js"></script>
<%--vuejs--%>
<script src="/static/plugins/vue.js"></script>
<%--webuploader--%>
<script src="/static/plugins/webuploader/webuploader.min.js"></script>
<script>

    /*定义一个全局数组,定义在外部便于存进去的值，往服务端发送的时候能取到值*/
    var fileArray = [];

    var days = 0;

    $(function () {

        /*派遣日期:默认今天momnet()，格式化format(),赋值val(xxx),取值val();*/

        $("#dispatchDate").val(moment().format("YYYY-MM-DD"));//注意momentjs与datepicker的格式大小写不同

        /*归还日期:$("#backDate").datepicker();
         * format:格式
         * language:语言
         * autoclose:选择后自动关闭
         * startDate:起始日期(支持moment()函数,同样需要格式化，不需要获取租赁日期文本框中的值，直接获取当前时间)，今天之前不能选(包括今天，最少一天)
         *
         * 该第三方插件，原生的失去焦点或者change都获取不到框内的值，只能使用其提供的事件
         * 链式调用on:datapicker的(event)
         * changeDate事件及其处理函数function(e){} e具有几个属性date;datas;format([ix],[format])
         * */
        $("#backDate").datepicker({
            format: "yyyy-mm-dd",
            language: "zh-CN",
            autoclose: true,
            startDate: moment().add(1,'days').format("YYYY-MM-DD")
        }).on("changeDate",function (e) {
            var dispatchDate = moment();
            var backDate = moment(e.format(0,'yyyy-mm-dd'));
            //e.format(0,'yyyy-mm-dd')获取选择的日期并格式化，
            // moment()转成moment格式对象以使用momentjs中的方法

            var totalDays = backDate.diff(dispatchDate,"days") + 1;
            //不能直接两个日期相减xxxx-xx-xx - yyyy-yy-yy或使用diff()方法，days表示相差天数,
            //diff()只能处理moment()类型的数据对moment()的格式无所谓
            $("#totalDays").val(totalDays);

            days = totalDays;

            //修改总价 在vue中实现的不完美 todo

        });
        /*momentjs的计算函数,日期时间计算交给插件*/
        /*var day1 = moment('2017-09-21');
         var day2 = moment('2017-09-22');
         var result = day2.diff(day1,'days');
         console.log(result);*/

        /**
         *模态框内的下拉框(默认带搜索功能)
         */
        $("#laborId").select2();

        /**
         * 下拉框改变事件
         * 每选择一次设备就要异步(ajax)查询改变一次页面内显示信息( todo 与数据库保持一致)
         */
        $("#laborId").change(function () {
            /*获取下拉框选中的选项option的值*/
            var id = $(this).val();
            $.get("/labor/dispatch/labor.json",{"id" : id}).done(function (result) {
                if(result.status == "success") {
                    /*查询成功，将数据在页面模态框内对应位置显示*/
                    var labor = result.data;
                    $("#currNum").val(labor.currentNum);
                    $("#unit").val(labor.unit);
                    $("#dispatchPrice").val(labor.price);

                    $("#laborName").val(labor.name);//工种选择列表中需要工种名称这个字段
                } else {
                    layer.msg(result.message);
                }
            }).error(function () {
                layer.msg("服务器忙，请稍后再试");
            });
        });

        // 初始化Web Uploader
        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,
            // swf文件路径
            swf: "/static/plugins/webuploader/Uploader.swf",
            // 文件接收服务端(该例为自己的服务器)
            server: "/file/upload",
            // 选择文件的按钮。div上传组件
            pick: '#picker',
            // form表单中<input type="file" name="file">的name属性
            fileVal:"file"

        });

        //uploader的API的events的uploadSuccess的两个参数file对象,response服务端返回的数据
        uploader.on( 'uploadSuccess', function(file,response) {
            layer.msg("上传成功！");

            //上传成功后在页面显示上传成功的文件原始名称
            var html = "<li>" + response.data.originalFilename + "</li>";
            $("#fileList").append(html);

            /*为了将数据存放到数据库，向客户端发送数据*/
            var json = {
                sourceName : response.data.originalFilename,
                newName : response.data.newFileName
            }
            /*每传成功一个就往数组里存放数据*/
            /*fileArray.push(response.data.newName)*/
            fileArray.push(json);

        });
        uploader.on( 'uploadError', function( file ) {
            layer.msg("服务器忙，上传失败！")
        });

    });

    /*vue框架不依赖于jquery
     * <div id="app">
     <input type="text" v-model="message">
     <p>{{message}}</p>
     </div>
     * var app = new Vue({
     *     el:"该层的div作为vue的组件(支持各种选择器)，div内所有东西被vue管理",
     *     data:{//数据
     *        message:xxx
     *     },
     *     methods:{//方法
     *     }
     *     computed:{//计算属性
     *     }
     * });
     *
     * SPA(single page application)单网页应用  VS   frameset/iframe地址栏不变
     * v-on:xxx
     * @xxx
     *
     * 路由
     * <a href="#/all"></a>
     *
     * 组件与数据绑定
     * []数组
     * {}对象
     * */
    var app = new Vue({
        el:"#app",
        data:{
            laborArray:[],//暂时定义一个空数组，
            days : days
            /*totalDays:$("#totalDays").val()*///租赁天数?怎么获取
        },
        methods:{
            addLabor:function () { //v-on:click="addLabor";鼠标点击时才会触发，将数据存入laborArray中
                //获取当前选择的工种的id,以及工人数量
                var laborId = $("#laborId").val();

                var currentNum = $("#currNum").val();

                //工人数量小于当前工种一次性要租雇佣的数量，（不严谨）//不加类型转换时，两个字符串相比，从第一个符号开始
                if(parseFloat(currentNum) < $("#dispatchNum").val()) {
                    layer.msg($("#laborName").val() + "人数不足");
                    //人数不足添加不进去，不成功
                } else {
                    //判断数组中是否有当前选择的工种，如果有则当前工种的数量增加,总价更新
                    var flag = false;
                    for(var i = 0;i < this.$data.laborArray.length;i++) {
                        var item = this.$data.laborArray[i];
                        if(item.id == laborId) {

                            /*item.num = parseFloat(item.num) + parseFloat($("#rentNum").val());
                             item.total = parseFloat(item.num) * parseFloat($("#rentPrice").val());*/
                            //不是java,没有java中的按地址引用,但以上方法可行

                            this.$data.laborArray[i].num = parseFloat(this.$data.laborArray[i].num) + parseFloat($("#dispatchNum").val());
                            this.$data.laborArray[i].total = parseFloat(this.$data.laborArray[i].num) * parseFloat($("#dispatchPrice").val());

                            /*//库存数量小于(页面内已租数量与当前要租的数量之和)data中的laborArray[]中存储的数据
                             if(currentNum < parseFloat(this.$data.laborArray[i].num)) {
                             layer.msg(this.$data.laborArray[i].name + "库存不足");
                             }*/

                            flag = true;
                            break;
                        }
                    }
                    //若果数组中没有当前选择的设备，则将数据直接添加进数组
                    if(!flag) {
                        var json = {};
                        json.id = laborId;//$("#laborId").val();不做显示用，作为条件判断的依据
                        json.name = $("#laborName").val();
                        json.unit = $("#unit").val();
                        json.price = $("#dispatchPrice").val();
                        json.num = $("#dispatchNum").val();
                        json.total = parseFloat(json.price) * parseFloat(json.num);

                        /*//工人数量小于data中的laborArray[]中存储的数据
                         if(currentNum < json.num) {
                         layer.msg(json.name + "工人不足");
                         }*/

                        this.$data.laborArray.push(json);
                        //(模态框中的数据赋值给json对象的对应属性)
                        // json数据添加到数据laborArray[]数组中
                        //通过vue的数据模块绑定，将laborArray数组中的数据循环(v-for="labor in laborArray")输出显示到工种选择列表中
                    }

                    //雇佣数量累加时，超过工人数量，暂无后续操作（不严谨）
                    if(currentNum < parseFloat(this.$data.laborArray[i].num)) {
                        layer.msg(this.$data.laborArray[i].name + "工人不足");

                        //页面内雇佣人数强制为最大数量
                        this.$data.laborArray[i].num = currentNum;
                        this.$data.laborArray[i].total = currentNum * parseFloat($("#dispatchPrice").val());
                    }
                }

            },
            //this.laborArray==this.$data.laborArray
            //删除，传参循环的对象labor；将labor对象从data中的对象数组laborArray中删除
            removeLabor:function (labor) {
                layer.confirm("确定要删除么？", function(index){
                    //splice(删除,插入,替换)从第几个开始（从数组中查询labor是第几个）删除几个
                    app.$data.laborArray.splice(app.$data.laborArray.indexOf(labor),1);
                    //点击窗口后，窗口自动消失
                    layer.close(index);
                });
            },
            //点击保存合同，向服务端发送数据（）
            saveRent:function () {
                var jsonObject = {
                    laborArray : app.$data.laborArray,
                    fileArray : fileArray,

                    companyName:$("#companyName").val(),
                    tel:$("#tel").val(),
                    dispatchDate:$("#dispatchDate").val(),
                    linkMan:$("#linkMan").val(),
                    address:$("#address").val(),
                    backDate:$("#backDate").val(),
                    cardNum:$("#cardNum").val(),
                    fax:$("#fax").val(),
                    totalDays:$("#totalDays").val()
                };

                // springMVC默认将对象/数组转换成json格式字符串(类似于servlet中的new Gson().toJson变成json格式的字符串"{"xx":"xx","xxx":"xxx"}"传输到jsp，
                // JSON.parse(String)   将JSON格式的字符串String-->JSON格式的对象Object！！！jsp中使用该方法将上字符串转换成json对象(对于ajax的请求，其会将结果根据相应的MIME头自动转换成相应格式，此时jsp中不用写这句话)

                //直接发送jsonObject过去时，ajax会将对象序列化成字符串"xx=xx&xx=xx&xx=xx"  服务端的方法参数中需要同时有多个xx键名参数去接收
                //jquery ajax send json
                //JSON.stringify(jsonObject)  将JSON格式的对象Object-->JSON格式的字符串String"{"xx":"xx","xx":"xx"}"传输到springMVC中,springMVC经过自动解析(jackson-databind)将其转换（封装）成对应对象进行接收，不用在方法的参数列表中写多个参数
                $.ajax({
                    url:"/labor/dispatch/new",
                    type:"post",
                    /*data:json,*/
                    data:JSON.stringify(jsonObject),
                    //发送给服务端的类型MIME头
                    contentType:"application/json;charset=UTF-8",
                    success:function (data) {
                        if(data.status == 'success') {
                            layer.confirm("保存成功",{btn:['继续添加','打印合同']},function(){
                                window.history.go(0);//刷新页面,继续添加
                            },function(){
                                window.location.href = "/labor/dispatch/"+data.data;//将合同流水号传递过来,打印时需要传递给客户端进行查询相关信息
                            });
                        } else {
                            layer.msg(data.message);
                        }
                    },
                    error:function () {
                        layer.msg("服务器忙，请稍后再试");
                    }
                });

            }
        },
        computed:{
            //this.$data只供获取data:{}节点内的数据
            total:function () {//等价于在数据节点data:{total:xxx}
                var result = 0;
                for(var i = 0;i < this.$data.laborArray.length;i++) {
                    var item = this.$data.laborArray[i].total;//从数据data项中的laborArray数组中取相应的值
                    result += item;
                }
                return result * parseFloat(days);
            },
            preCost:function () {
                var result = this.total * 0.3;//this.total从当前节点中取值当前的total；
                return result;
            },
            lastCost:function () {
                var result = this.total - this.preCost;
                return result;
            }
        }
    });
</script>
</body>
</html>
