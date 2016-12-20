$(function () {

    /*修改邮件*/
    $("#baseBtn").click(function () {
        $("#baseForm").submit();
    });

    $("#baseForm").validate({
        errorElement:"span",
        errorClass:"text-error",
        rules:{
            email:{
                required:true,
                email:true,
                remote:"/validate/email?type=reset"
            }
        },
        messages:{
            email:{
                required:"请输入电子邮件",
                email:"电子邮件格式错误",
                remote:"该电子邮件已被占用"
            }
        },
        submitHandler:function (form) {
            $.ajax({
                url:"/setting?action=profile",
                type:"post",
                data:$(form).serialize(),
                beforeSend:function () {
                    $("#baseBtn").append("<i class='fa fa-spinner fa-spin'></i>").attr("disabled","disabled");
                },
                success:function (data) {
                    swal({
                        title: "修改成功",
                        text: "两秒后自动关闭",
                        timer: 2000,
                        showConfirmButton: true
                    });
                    /*swal("Good job!", "修改成功,请到邮箱中收取邮件并激活账号", "success");*/
                },
                error:function () {
                    swal("Oops!", "服务器错误", "error");
                },
                complete:function () {
                    $("#baseBtn").html("保存").removeAttr("disabled");
                }
            });
        }
    });
    
    /*修改密码*/
    $("#pswBtn").click(function () {
        $("#pswForm").submit();
    });
    
    $("#pswForm").validate({
        errorElement:"span",
        errorClass:"text-error",
        rules:{
            oldpassword:{
                required:true,
                rangelength:[6,18]
            },
            newpassword:{
                required:true,
                rangelength:[6,18]
            },
            repassword:{
                required:true,
                rangelength:[6,18],
                equalTo:"#newpassword"
            }
        },
        messages:{
            oldpassword:{
                required:"请输入原始密码",
                rangelength:"密码长度6-18个字符"
            },
            newpassword:{
                required:"请输入新密码",
                rangelength:"密码长度6-18个字符"
            },
            repassword:{
                required:"请输入确认密码",
                rangelength:"密码长度6-18个字符",
                equalTo:"两次密码不一致"
            }
        },
        submitHandler:function (form) {
            $.ajax({
                url:"/setting?action=password",
                type:"post",
                data:$(form).serialize(),
                beforeSend:function () {
                    $("#pswBtn").append("<i class='fa fa-signer fa-sign'></i>").attr("disabled","disabled");
                },
                success:function (data) {
                    if(data.state == "success") {
                        swal({
                                title: "修改成功",
                                text: "重新登陆",
                                type: "success",
                                showCancelButton: false,
                                confirmButtonColor: "#4285F4",
                                confirmButtonText: "",
                                //cancelButtonText: "回到主页",
                                closeOnConfirm: false,
                                //closeOnCancel: false
                            },
                            function(isConfirm){
                                if (isConfirm) {
                                    window.location.href = "/login";
                                }/* else {
                                    window.location.href = "/home";
                                }*/
                            });
                    } else {
                        swal("Oops!", data.message, "error");
                    }
                },
                error:function () {
                    swal("Oops!", "服务器错误", "error");
                },
                complete:function () {
                    $("#pswBtn").html("保存").removeAttr("disabled");
                }
            });
        }
    });

});
