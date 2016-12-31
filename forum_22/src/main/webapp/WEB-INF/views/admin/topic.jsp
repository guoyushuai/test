<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>主题管理</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">
    <style>
        .table td {
            vertical-align: middle;
        }
        select {
            width: 70px;
            margin: 0px;
        }
    </style>
</head>
<body>

<%@ include file="../include/admin_navbar.jsp"%>
<!--header-bar end-->
<div class="container-fluid" style="margin-top:20px">
    <table class="table">
        <thead>
        <tr>
            <th>名称</th>
            <th>发布人</th>
            <th>发布时间</th>
            <th>回复数量</th>
            <th>最后回复时间</th>
            <th>所属节点</th>
            <th colspan="2" style="text-align:center">操作</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${requestScope.page.items}" var="topic"><%--varStatus="vs"--%>
            <tr>
                <td>
                    <a href="/topicDetail?topicid=${topic.id}" target="_blank">${topic.title}</a>
                </td>
                <td>${topic.user.username}</td>
                <td>${topic.createtime}</td>
                <td>${topic.replynum}</td>
                <td>${topic.lastreplytime}</td>
                <td>
                    <%--循环体内部不能用id选择器--%>
                    <select name="nodeid" class="nodeid">
                        <option value="">请选择节点</option>
                        <c:forEach items="${requestScope.nodeList}" var="node">
                            <%--默认选中该主题所属的节点，此topic对象没有封装node--%>
                            <option ${topic.nodeid == node.id?'selected':''} value="${node.id}">${node.nodename}</option>
                        </c:forEach>
                    </select>
                </td>
                <td style="text-align:center"><a href="javascript:;" rel="${topic.id}" class="update">修改</a></td>
                    <%--不能直接跳转去删除，需要询问一下。循环不能用id选择器--%>
                <td style="text-align:center"><a href="javascript:;" rel="${topic.id}" class="delete">删除</a></td>
            </tr>
        </c:forEach>

        </tbody>
    </table>

    <div class="pagination pagination-mini pagination-centered">
        <ul id="pagination" style="margin-bottom:20px;"></ul>
    </div>

</div>
<!--container end-->
<script src="/static/js/jquery-1.11.1.js"></script>
<script src="/static/js/sweetalert.min.js"></script>
<script src="/static/js/jquery.twbsPagination.min.js"></script>
<script>
    $(function () {
        $("#pagination").twbsPagination({
            totalPages:${page.totalPage},
            visiblePages:5,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href: '?p={{number}}&_=1'
        });

        /*修改标签的点击事件*/
        $(".update").click(function () {
           var topicid = $(this).attr("rel");
           /*下拉框中被选中的节点的value属性的值 */
           /*循环体中id选择器这样获取的永远是页面内第一个下拉框内的值
            var nodeid = $("#nodeid").val();*/

           var nodeid = this.parentNode.parentNode.querySelector("select").value;

           alert(topicid);
           alert(nodeid);
           $.ajax({
               url:"/admin/topic?action=update",
               type:"post",
               data:{"topicid":topicid,"nodeid":nodeid},
               success:function (result) {
                    if(result.state == "success") {
                        swal({
                            title: "修改成功",
                            text: "",
                            type: "success"
                        },function () {
                            /*0-刷新，1-前进，-1-后退*/
                            window.history.go(0);
                        });
                    } else {
                        swal("修改失败!", result.message, "error");
                   }
               },
               error:function () {
                   swal("Oops!", "服务器错误", "error");
               }
           })
        });

        /*每一个主题的删除链接都绑定click事件*/
        $(".delete").click(function () {
            /*进一步获取准确的要删除的主题的id*/
            var topicid = $(this).attr("rel");
            swal({
                title: "确定要删除该主题?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                closeOnConfirm: false
                },
                function () {
                    $.ajax({
                        url:"/admin/topic?action=delete",
                        type:"post",
                        data:{"topicid":topicid},
                        success:function (result) {
                            if(result.state == "success") {
                                swal({
                                    title: "删除成功",
                                    text: "",
                                    type: "success",
                                },function () {
                                    window.history.go(0);
                                });
                            } else {
                                swal("删除失败", result.message, "error");
                            }
                        },
                        error:function () {
                            swal("Oops!", "服务器错误", "error");
                        }
                    })
                }
            )
        });
    });
</script>
</body>
</html>
