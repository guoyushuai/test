$(function () {

    $("#resetBtn").click(function () {
        $("#resetForm").submit();
    });

    $("#resetForm").validate({
        errorElement:"span",
        errorClass:"text-error",
        rules:{
            password:{
                required:true,
                rangelength:[6,18]
            },
            repassword:{
                required:true,
                rangelength:[6,18],
                equalTo:"#password"
            }
        },
        messages:{
            password:{
                required:"请输入密码",
                rangelength:"密码为6-18位"
            },
            repassword:{
                required:"请输入确认密码",
                rangelength:"密码为6-18位",
                equalTo:"两次密码不一致"
            }
        },
        submitHandler:function () {
            $.ajax({
                url:"/resetPassword",
                type:"post",
                data:$("#resetForm").serialize(),
                beforeSend:function () {
                    $("#resetBtn").append("<i class='fa-spinner fa-spin'></i>>").attr("disabled","disabled");
                },
                success:function (data) {
                    if(data.state == "success") {
                        alert("密码重置成功，请登录");
                        window.location.href = "/login";
                    } else {
                        alert(data.message);
                    }
                },
                error:function () {
                    alert("服务器错误");
                },
                complete:function () {
                    $("#resetBtn").html("保存").removeAttr("disabled");
                }
            })
        }
    })

});
