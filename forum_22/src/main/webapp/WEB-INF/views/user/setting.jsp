<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户设置</title>
    <link href="/static/css/font-awesome.min.css" rel="stylesheet">
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/sweetalert.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/js/uploader/webuploader.css">
    <link rel="stylesheet" href="/static/css/style.css">

</head>
<body>

<%@include file="../include/navbar.jsp"%>

<!--header-bar end-->
<div class="container">
    <div class="box">
        <div class="box-header">
            <span class="title"><i class="fa fa-cog"></i> 基本设置</span>
        </div>

        <form action="" class="form-horizontal" id="baseForm">
            <div class="control-group">
                <label class="control-label">账号</label>
                <div class="controls">
                    <input type="text" readonly value="${sessionScope.current_user.username}">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">电子邮件</label>
                <div class="controls">
                    <input type="text" value="${sessionScope.current_user.email}" name="email">
                </div>
            </div>
            <div class="form-actions">
                <button class="btn btn-primary" type="button" id="baseBtn">保存</button>
            </div>

        </form>

    </div>
    <!--box end-->
    <div class="box">
        <div class="box-header">
            <span class="title"><i class="fa fa-key"></i> 密码设置</span>
            <span class="pull-right muted" style="font-size: 12px">如果你不打算更改密码，请留空以下区域</span>
        </div>

        <form action="" class="form-horizontal" id="pswForm">
            <div class="control-group">
                <label class="control-label">原始密码</label>
                <div class="controls">
                    <input type="password" name="oldpassword" >
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">新密码</label>
                <div class="controls">
                    <input type="password" name="newpassword" id="newpassword">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">重复密码</label>
                <div class="controls">
                    <input type="password" name="repassword">
                </div>
            </div>
            <div class="form-actions">
                <button class="btn btn-primary" type="button" id="pswBtn">保存</button>
            </div>

        </form>

    </div>
    <!--box end-->

    <div class="box">
        <div class="box-header">
            <span class="title"><i class="fa fa-user"></i> 头像设置</span>
        </div>

        <form action="" class="form-horizontal">
            <div class="control-group">
                <label class="control-label">当前头像</label>
                <div class="controls">
                    <img id="avatar" src="http://oi0nv2rk1.bkt.clouddn.com/${sessionScope.current_user.avatar}?imageView2/1/w/40/h/40" class="img-circle" alt="">
                </div>
            </div>
            <hr>
            <p style="padding-left: 20px">关于头像的规则</p>
            <ul>
                <li>禁止使用任何低俗或者敏感图片作为头像</li>
                <li>如果你是男的，请不要用女人的照片作为头像，这样可能会对其他会员产生误导</li>
            </ul>
            <div class="form-actions">
                <%--百度uploader要求上传键为div--%>
                <div id="picker">上传新头像</div>
            </div>

        </form>

    </div>
    <!--box end-->

</div>
<!--container end-->

<script src="/static/js/jquery-1.11.1.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script src="/static/js/sweetalert.min.js"></script>
<script src="/static/js/uploader/webuploader.min.js"></script>
<script src="/static/js/user/setting.js"></script>


<%--写在外部js文件中获取不到token的值--%>
<script>
    $(function () {
        var uploader = WebUploader.create({

            // swf文件路径
            swf:"/static/js/uploader/Uploader.swf",

            // 文件接收服务端(七牛云存储)
            server: 'http://up-z1.qiniu.com/',

            // 选择文件的按钮
            pick: '#picker',

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },

            // 选完文件后，是否自动上传。
            auto: true,

            //设置文件上传域的name。默认值file
            fileVal:"file",

            //文件上传请求的参数表，每次发送都会发送此对象中的参数(七牛云要求为json数据)
            //放在外部js文件中，el表达式娶不到token值,json数据格式记得加引号哦
            formData: {"token":"${token}"}
            //该token通过/setting的doget方法传递过来
        });

        ////上传成功，File对象,data服务端返回的数据（七牛{hash:"",key:""}）data.key服务端存储的文件名
        uploader.on( 'uploadSuccess', function( file,data ) {
            //向服务器发送数据，修改数据库中的值
            $.ajax({
                url:"/setting?action=avatar",
                type:"post",
                data:{"avatarname":data.key},
                success:function (result) {
                    if(result.state == "success") {
                        swal("Good job!", "设置成功", "success");
                        //修改页面中两处显示头像的地方的图片
                        var url = "http://oi0nv2rk1.bkt.clouddn.com/" + data.key;
                        $("#avatar").attr("src",url+"?imageView2/1/w/40/h/40");
                        $("#navbar_avatar").attr("src",url+"?imageView2/1/w/20/h/20");
                    }
                },
                error:function () {
                    swal("Oops!","设置失败，请稍后再试", "error");
                }
            });
        });

        uploader.on( 'uploadError', function() {
            swal("Oops!","上传失败，请稍后再试", "error");
        });
    });

</script>

</body>
</html>