$(function () {

    $("#btn").click(function () {
        $("#loginForm").submit();
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
                    $("#btn").append($("<i class='fa fa-refresh' fa-spin></i>")).attr("disabled","disabled")
                },
                success:function (data) {
                    if(data.state == "success") {
                        alert("登录成功");
                        window.location.href = "/home";
                    } else {
                        alert(data.message);
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
