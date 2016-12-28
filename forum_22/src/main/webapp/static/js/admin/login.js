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

    $("#loginBtn").click(function () {
        $("#loginForm").submit();
    });

    //回车键，提交表单
    $("#password").keydown(function () {
        /*event.keyCode键值*/
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
                url:"/admin/login",
                type:"post",
                data:$("#loginForm").serialize(),
                beforeSend:function () {
                    $("#loginBtn").append($("<i class='fa fa-spinner fa-spin'></i>")).attr("disabled","disabled");
                },
                success:function (result) {
                    if(result.state == "success") {
                        swal({
                            title: "登录成功",
                            text: "",
                            type: "success",
                        },function () {
                            //swal插件在弹框后如果有函数要执行，会一闪而过，防止swal插件一闪而过,将要执行的函数写进swal提供的函数体内

                            //之前表单提交页面刷新是在LoginServlet的dopost中通过获取url中的callback进行判断，登录成功后跳转到原请求界面

                            //不传url,获取当前地址栏（redirect=后，#reply前）的值5
                            var uri = getParameterByName("redirect");
                            //获取锚标记#后(包括#)的值，以便于回调后定位到页面指定位置
                            var reply = location.hash;
                            /*alert(uri + "->" + reply);*/
                            if(uri) {
                                if(reply) {
                                    window.location.href = uri + reply;

                                } else {
                                    window.location.href = uri;
                                }
                            } else {
                                window.location.href = "/admin/home";
                            }
                        });
                    } else {
                        swal("登录失败", result.message, "error");
                        /*if(data.message == "账号还未激活") {
                         //1请到邮箱中激活账号
                         //2链接已过期或者邮件发送失败点击按钮重新发送邮件（ajax请求）
                         }*/
                    }
                },
                error:function () {
                    swal("Oops!", "服务器错误", "error");
                },
                complete:function () {
                    $("#loginBtn").html("登录").removeAttr("disabled");
                }
            });
        }
    });

})

