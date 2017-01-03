<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>节点管理</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">
    <style>
        .mt20 {
            margin-top: 20px;
        }
    </style>
</head>
<body>

<%@ include file="../include/admin_navbar.jsp"%>

<!--header-bar end-->
<div class="container-fluid mt20">
    <a href="javascript:;" class="btn btn-success" id="newnode">添加新节点</a>
    <table class="table">
        <thead>
        <tr>
            <th>节点名称</th>
            <th colspan="2" style="text-align: center">操作</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${requestScope.nodeList}" var="node" varStatus="vs">
            <tr>
                <td><span class="nodename" <%--value="${node.nodename}" rel="${vs.count}"--%>>${node.nodename}</span></td>
                <td style="text-align: right"><a href="javascript:;" class="editnode" rel="${node.id}">修改</a></td>
                <td style="text-align: left"><a href="javascript:;" class="delnode" rel="${node.id}">删除</a></td>
            </tr>
        </c:forEach>

        </tbody>
    </table>
</div>
<!--container end-->
<script src="/static/js/jquery-1.11.1.js"></script>
<script src="/static/js/sweetalert.min.js"></script>

<script>

    $("#newnode").click(function () {
        swal({
                title: "new！",
                text: "输入新节点名称：",
                type: "input",
                showCancelButton: true,
                cancelButtonText: "取消！",
                confirmButtonText: "确定！",
                    /*closeOnCancel: false,*/
                closeOnConfirm: false,
                animation: "slide-from-top",
                inputPlaceholder: "new node"
            },
            function(inputValue){
                if (inputValue === false) {
                    return false;
                } else if (inputValue === "") {
                    swal.showInputError("节点名称不能为空！");
                    return false;
                } else {
                    /*//获取所有class属性值为nodename的节点
                     var $nodename = $(".nodename");
                     for(var i = 0;i < $nodename.length;i++) {
                     //取不到值
                     $nodemame[i].innerText；
                     $nodemame[i].innerHtml;
                     $nodemame[i].value;
                     $nodemame[i].val();
                     $nodemame[i].text();
                     $nodemame[i].html();
                     }*/

                    //做个flag
                    var flag = true;
                    $(".nodename").each(function(){
                        var nodename = $(this).text();

                        if(inputValue === nodename) {
                            flag = false;
                        }
                    });

                    if(!flag) {
                        swal.showInputError("节点已经存在！");
                        return false;
                    } else {
                        /*swal("非常好！", "新节点名称：" + inputValue,"success");*/
                        swal({
                                title: "新节点名称:" + inputValue,
                                text: "点击ok提交",
                                type: "info",
                                showCancelButton: true,
                                closeOnConfirm: false,
                                showLoaderOnConfirm: true,
                            },
                            function(){
                                setTimeout(function(){
                                    var newnodename = inputValue;
                                    $.ajax({
                                        url:"/admin/node?action=new",
                                        type:"post",
                                        data:{"newnodename":newnodename},
                                        success:function (result) {
                                            if(result.state == "success") {
                                                swal({
                                                    title: "添加成功",
                                                    text: "",
                                                    type: "success",
                                                },function () {
                                                    window.history.go(0);
                                                });
                                            } else {
                                                swal("Oops!",result.message, "error");
                                            }
                                        },
                                        error:function () {
                                            swal("Oops!", "服务器错误", "error");
                                        }
                                    });
                                });
                            });

                        /*swal({
                         tltle:"good" ,
                         text:"新节点名称" + inputValue,
                         type:"success"
                         }, function () {

                         });*/
                    }
                }
            });

    });

    $(".delnode").click(function () {
        var nodeid = $(this).attr("rel");
        swal({
                title: "delete",
                text:"确定要删除该节点?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                closeOnConfirm: false
            },
            function(){
                $.ajax({
                    url:"/admin/node?action=delete",
                    type:"post",
                    data:{"nodeid":nodeid},
                    success:function (result) {
                        if(result.state == "success") {
                            swal({title:"删除成功"}, function () {
                                window.history.go(0);
                            });
                        } else {
                            swal("Oops!", result.message, "error");
                        }
                    },
                    error:function () {
                        swal("Oops!", "服务器错误", "error");
                    }
                })
            }
        );
    });

    $(".editnode").click(function () {
        var nodeid = $(this).attr("rel");
        swal({
                title: "edit！",
                text: "输入新节点名称：",
                type: "input",
                showCancelButton: true,
                cancelButtonText: "取消修改！",
                confirmButtonText: "确定修改！",
                /*closeOnCancel: false,*/
                closeOnConfirm: false,
                animation: "slide-from-top",
                inputPlaceholder: "new node"
            },
            function(inputValue){
                if (inputValue === false) {
                    return false;
                } else if (inputValue === "") {
                    swal.showInputError("节点名称不能为空！");
                    return false;
                } else {

                    var flag = true;
                    $(".nodename").each(function(){
                        var nodename = $(this).text();

                        if(inputValue === nodename) {
                            flag = false;
                        }
                    });

                    if(!flag) {
                        swal.showInputError("节点已经存在！");
                        return false;
                    } else {
                        /*swal("非常好！", "新节点名称：" + inputValue,"success");*/
                        swal({
                                title: "新节点名称:" + inputValue,
                                text: "点击ok提交",
                                type: "info",
                                showCancelButton: true,
                                cancelButtonText: "放弃修改！",
                                confirmButtonText: "确定修改！",
                                closeOnConfirm: false,
                                closeOnCancel: false,
                                showLoaderOnConfirm: true,
                            },
                            function(){
                                setTimeout(function(){
                                    var newnodename = inputValue;
                                    $.ajax({
                                        url:"/admin/node?action=edit",
                                        type:"post",
                                        data:{"nodeid":nodeid,"newnodename":newnodename},
                                        success:function (result) {
                                            if(result.state == "success") {
                                                swal({
                                                    title: "修改成功",
                                                    text: "",
                                                    type: "success",
                                                },function () {
                                                    window.history.go(0);
                                                });
                                            } else {
                                                swal("Oops!",result.message, "error");
                                            }
                                        },
                                        error:function () {
                                            swal("Oops!", "服务器错误", "error");
                                        }
                                    });
                                });
                            });
                    }
                }
            });
    });

</script>

</body>
</html>

