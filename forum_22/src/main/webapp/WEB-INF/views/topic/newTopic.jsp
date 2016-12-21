<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>发布新主题</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/js/editer/styles/simditor.css">
</head>
<body>

<%@ include file="../include/navbar.jsp" %>

<!--header-bar end-->
<div class="container">
    <div class="box">
        <div class="box-header">
            <span class="title"><i class="fa fa-plus"></i> 发布新主题</span>
        </div>

        <form action="" style="padding: 20px" id="sendForm">
            <label class="control-label">主题标题</label>
            <input name="title" type="text" style="width: 100%;box-sizing: border-box;height: 30px"
                   placeholder="请输入主题标题，如果标题能够表达完整内容，则正文可以为空">
            <label class="control-label">正文</label>
            <textarea name="content" id="editor"></textarea>

            <select name="nodeid" style="margin-top:15px;">
                <option value="">请选择节点</option>
                <c:if test="${not empty requestScope.nodeList}">
                    <c:forEach items="${requestScope.nodeList}" var="node">
                        <%--<option value="">${node}</option>--%>
                        <option value="${node.id}">${node.nodename}</option>
                    </c:forEach>
                </c:if>
            </select>

        </form>
        <div class="form-actions" style="text-align: right">
            <button class="btn btn-primary" id="sendBtn">发布主题</button>
        </div>


    </div>
    <!--box end-->
</div>
<!--container end-->
<script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
<script src="/static/js/editer/scripts/module.min.js"></script>
<script src="/static/js/editer/scripts/hotkeys.min.js"></script>
<script src="/static/js/editer/scripts/uploader.min.js"></script>
<script src="/static/js/editer/scripts/simditor.min.js"></script>
<script src="/static/js/topic/topic.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script src="/static/js/sweetalert.min.js"></script>
<script>
    $(function () {
        var editor = new Simditor({
            textarea: $('#editor')
            //optional options
        });

        //文本域写在外部js中，用不了

        $("#sendBtn").click(function () {
            $("#sendForm").submit();
        });

        $("#sendForm").validate({
            errorElement: "span",
            errorClass: "text-error",
            rules: {
                title: {
                    required: true
                },
                nodeid: {
                    required: true
                }
            },
            messages: {
                title: {
                    required: "请输入标题"
                },
                nodeid: {
                    required: "请选择节点"
                }
            },
            submitHandler: function (form) {
                $.ajax({
                    url: "/newTopic",
                    type: "post",
                    data: $(form).serialize(),
                    beforeSend: function () {
                        $("#sendBtn").append($("<i class='fa fa-spinner fa-spin'></i>")).attr("disabled", "disabled");
                    },
                    success: function (result) {
                        if (result.state == "success") {
                            swal({
                                    title: "发布成功",
                                    text: "点击前往查看帖子详情",
                                    type: "success",
                                    timer: 2000,
                                    showCancelButton: false,
                                    confirmButtonColor: "#4285F4",
                                    confirmButtonText: "前往",
                                    closeOnCancel: true
                                },
                                function (isConfirm) {
                                    if (isConfirm) {
                                        //发帖成功，跳转到帖子详情页面
                                        /*window.location.href = "/topicDetail";*/
                                        //指定现实哪一条帖子，需要在跳转的同时传递帖子id,/*map<"data",topic>*/是data.id,不是topic.id
                                        window.location.href = "/topicDetail?topicid=" + result.data.id;
                                    }/* else {
                                        window.location.href = "/home";
                                    }*/
                                });
                        } else {
                            swal("Oops!", "发帖失败", "error");
                        }

                    },
                    error: function () {
                        swal("Oops!", "服务器异常，请稍后再试", "error");
                    },
                    complete: function () {
                        $("#btn").html("发布主题").removeAttr("disabled");
                    }

                })
            }
        });

    });
</script>

</body>
</html>