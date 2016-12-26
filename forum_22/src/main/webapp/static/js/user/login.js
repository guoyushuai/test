$(function () {

    //类似request.getParameter("key"),根据键找对应的值javaScript中没有内置这个方法
    function getParameterByName(name, url) {
        if (!url) {
            //当前地址栏中的url
            url = window.location.href;
        }
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    $("#btn").click(function () {
        $("#loginForm").submit();
    });

    //回车键，提交表单
    $("#password").keydown(function () {
        if(event.keyCode == "13") {
            /*$("#btn").click();*/
            $("#loginForm").submit();
        }
    });

    $("#loginForm").validate({
        errorElement:"span",
        errorClass:"text-error",
        rules:{
            username:{
                required:true
            },
            password:{
                required:true
            }
        },
        messages:{
            username:{
                required:"请输入账号"
            },
            password:{
                required:"请输入密码"
            }
        },
        submitHandler:function () {
            $.ajax({
                url:"/login",
                type:"post",
                data:$("#loginForm").serialize(),
                beforeSend:function () {
                    $("#btn").append($("<i class='fa fa-spinner fa-spin'></i>")).attr("disabled","disabled");
                },
                success:function (data) {
                    if(data.state == "success") {
                        alert("登录成功");
                        //之前表单提交页面刷新是在LoginServlet的dopost中通过获取url中的callback进行判断，登录成功后跳转到原请求界面

                        //不传url,获取当前地址栏，只传name
                        var uri = getParameterByName("redirect");
                        //获取锚标记#后的值，以便于回调后定位到页面指定位置
                        var reply = location.hash;
                        if(uri) {
                            if(reply) {
                                window.location.href = uri + reply;

                            } else {
                                window.location.href = uri;
                            }
                        } else {
                            window.location.href = "/home";
                        }

                    } else {
                        alert(data.message);
                        /*if(data.message == "账号还未激活") {
                            //1请到邮箱中激活账号
                            //2链接已过期或者邮件发送失败点击按钮重新发送邮件（ajax请求）
                        }*/
                    }
                },
                error:function () {
                    alert("服务器出错");
                },
                complete:function () {
                    $("#btn").html("登录").removeAttr("disabled");
                }
            });
        }
    });

})
