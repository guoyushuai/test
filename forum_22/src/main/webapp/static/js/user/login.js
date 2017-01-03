$(function () {
    /*$("#change").click(function () {
        $("#img").removeAttr("src").attr("src","/captcha.png?_=" + new Date().getTime());
    });*/

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


    /*var hash = {
        'qq.com'		: 'http://mail.qq.com',
        'gmail.com'		: 'http://mail.google.com',
        'vip.sina.com'	: 'http://mail.sina.com.cn',
        '163.com'		: 'http://mail.163.com',
        '126.com'		: 'http://mail.126.com',
        'yeah.net'		: 'http://www.yeah.net/',
        'sohu.com'		: 'http://mail.sohu.com/',
        'tom.com'		: 'http://mail.tom.com/',
        'sogou.com'		: 'http://mail.sogou.com/',
        '139.com'		: 'http://mail.10086.cn/',
        'hotmail.com'	: 'http://www.hotmail.com',
        'live.com'		: 'http://login.live.com/',
        'live.cn'		: 'http://login.live.cn/',
        'live.com.cn'	: 'http://login.live.com.cn',
        '189.com'		: 'http://webmail16.189.cn/webmail/',
        'yahoo.com.cn'	: 'http://mail.cn.yahoo.com/',
        'yahoo.cn'		: 'http://mail.cn.yahoo.com/',
        'eyou.com'		: 'http://www.eyou.com/',
        '21cn.com'		: 'http://mail.21cn.com/',
        '188.com'		: 'http://www.188.com/',
        'foxmail.coom'	: 'http://www.foxmail.com'
    };
    var domain = email.split('@')[1];
    $.genJumpMailUrl = function(){
        if (hash[domain]) {
            return hash[domain];
        }
        return "http://mail." + domain;
    }
    function jump(){window.open($.genJumpMailUrl());}*/

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
                                window.location.href = "/home";
                            }
                        });
                    } else {
                        if(data.message == "账号还未激活") {
                            var username = $("#username").val();
                            var jumpemail = data.email;
                            /*alert(jumpemail);*/
                            swal({
                                    title: data.message,
                                    text: "请到注册邮箱中查收激活邮件",
                                    type: "warning",
                                    showCancelButton: true,
                                    confirmButtonColor: "#2977dd",
                                    cancelButtonColor:"#dd0015",
                                    confirmButtonText: "前往登录邮箱",
                                    cancelButtonText: "未收到邮件！",
                                    closeOnConfirm: false,
                                    closeOnCancel: false
                                },
                                function(isConfirm){
                                    if (isConfirm) {
                                        /*swal("Oops!", "请前往登录邮箱", "success");*/
                                        var url = jumpemail.split('@')[1];
                                        /*alert(url);*/
                                        window.location.href = "http://mail."+url;

                                    } else {
                                        swal({
                                            title: "点击重新发送邮件",
                                            text: "",
                                            type: "info",
                                            closeOnConfirm: false
                                        },function () {
                                            $.ajax({
                                                url:"/user/active",
                                                type:"post",
                                                data:{"email":data.email},
                                                success:function (result) {
                                                    if(result.state == "success") {
                                                        /*alert("邮件已发送,前往登录邮箱！");
                                                        * closeOnConfirm: false */
                                                        swal({
                                                             title: "邮件已发送",
                                                             text: "请登录邮箱",
                                                             type: "success",
                                                             },function () {
                                                             var url = jumpemail.split('@')[1];
                                                             /*alert(url);*/
                                                             window.location.href = "http://mail." + url;
                                                         });
                                                        /*var url = jumpemail.split('@')[1];
                                                        window.location.href = "http://mail." + url;*/

                                                    } else {
                                                        swal("Oops!", result.message, "error");
                                                    }
                                                },
                                                error:function () {
                                                    swal("Oops!", "服务器错误", "error");
                                                }
                                            });
                                        });
                                    }
                                });
                        } else if(data.message == "账号已被禁用") {
                            swal(data.message, "请联系管理员", "error");
                        } else {
                            swal("登录失败!", data.message, "error");
                        }
                    }
                },
                error:function () {
                    swal("Oops!", "服务器错误", "error");
                },
                complete:function () {
                    $("#btn").html("登录").removeAttr("disabled");
                }
            });
        }
    });

})
