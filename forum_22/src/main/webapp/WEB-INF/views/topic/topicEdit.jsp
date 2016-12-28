<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>修改主题</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/js/editer/styles/simditor.css">
    <link rel="stylesheet" href="/static/js/editer/emoji/simditor-emoji.css">
    <link rel="stylesheet" href="/static/js/editer/highlight/styles/default.css">
</head>
<body>

<%@ include file="../include/navbar.jsp" %>

<!--header-bar end-->
<div class="container">
    <div class="box">
        <div class="box-header">
            <span class="title"><i class="fa fa-edit"></i> 修改主题</span>
        </div>

        <form action="" style="padding: 20px" id="sendForm">
            <label class="control-label">主题标题</label>
            <%--post提交时要给服务端确定修改的是哪一个帖子--%>
            <input type="hidden" name="topicid" value="${requestScope.topic.id}">

            <input name="title" type="text" style="width: 100%;box-sizing: border-box;height: 30px"
                   value="${requestScope.topic.title}">
            <label class="control-label">正文</label>
            <textarea name="content" id="editor">${requestScope.topic.content}</textarea>

            <select name="nodeid" style="margin-top:15px;">
                <option value="">请选择节点</option>
                <c:if test="${not empty requestScope.nodeList}">
                    <c:forEach items="${requestScope.nodeList}" var="node">
                        <%--<option value="">${node}</option>--%>
                        <%--默认选中修改前选择的节点--%>
                        <option ${topic.nodeid == node.id ? 'selected' : ''} value="${node.id}">${node.nodename}</option>
                    </c:forEach>
                </c:if>
            </select>

        </form>
        <div class="form-actions" style="text-align: right">
            <button class="btn btn-primary" id="sendBtn">修改主题</button>
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
<script src="/static/js/editer/emoji/simditor-emoji.js"></script>
<script src="/static/js/editer/highlight/highlight.pack.js"></script>
<script>
    $(function () {
        var editor = new Simditor({
            textarea: $('#editor'),
            //optional options
            toolbar: [
                'title',
                'bold',
                'italic',
                'underline',
                'strikethrough',
                'fontScale',
                'color',
                'ol',
                'ul',
                'blockquote',
                'code',
                'table',
                'link',
                'image',
                'hr',
                'indent',
                'outdent',
                'alignment',
                'emoji'
            ],
            emoji: {
                imagePath: '/static/js/editer/emoji/emoji/',
                /*images: [
                 'smile.png','smiley.png','laughing.png','blush.png','heart_eyes.png','smirk.png','flushed.png','grin.png','wink.png','kissing_closed_eyes.png','stuck_out_tongue_winking_eye.png','stuck_out_tongue.png','sleeping.png','worried.png','expressionless.png','sweat_smile.png','cold_sweat.png','joy.png','sob.png','angry.png','mask.png','scream.png','sunglasses.png','heart.png','broken_heart.png','star.png','anger.png','exclamation.png','question.png','zzz.png','thumbsup.png','thumbsdown.png','ok_hand.png','punch.png','v.png','clap.png','muscle.png','pray.png','skull.png','trollface.png'
                 ]*/
            },
            upload:{
                url:"http://up-z1.qiniu.com/",
                params:{"token":"${token}"},
                fileKey:"file"
            }
        });
        hljs.initHighlightingOnLoad();


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
                    url: "/topicEdit",
                    type: "post",
                    data: $(form).serialize(),
                    beforeSend: function () {
                        $("#sendBtn").append($("<i class='fa fa-spinner fa-spin'></i>")).attr("disabled", "disabled");
                    },
                    success: function (result) {
                        if (result.state == "success") {
                            swal({
                                    title: "编辑成功",
                                    text: "点击前往查看帖子详情",
                                    type: "success",
                                },
                                function () {
                                    //修改成功，跳转到帖子详情页面
                                    /*window.location.href = "/topicDetail";*/
                                    //指定现实哪一条帖子，需要在跳转的同时传递帖子id
                                    window.location.href = "/topicDetail?topicid=" + result.data;
                                });
                        } else {
                            swal({
                                    title: "编辑失败",
                                    text: result.message,
                                    type: "error",
                                },
                                function () {
                                        //修改失败，跳转到帖子详情页面
                                        window.location.href = "/topicDetail?topicid=" + result.data;
                                });
                        }

                    },
                    error: function () {
                        swal("Oops!", "服务器异常，请稍后再试", "error");
                    },
                    complete: function () {
                        $("#btn").html("修改主题").removeAttr("disabled");
                    }

                })
            }
        });

    });
</script>

</body>
</html>