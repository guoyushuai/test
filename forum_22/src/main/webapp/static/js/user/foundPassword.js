$(function () {

    $("#type").change(function () {
        var option = $(this).val();
        if("email" == option) {
            $("#typename").text("电子邮件");
        } else if("phone" == option) {
            $("#typename").text("手机号码");
        }
    });

    $("#foundBtn").click(function () {
        $("#foundForm").submit();
    });

    $("#foundForm").validate({
        errorElement:"span",
        errorClass:"text-error",
        rules:{
            value:{
                required:true
            }
        },
        messages:{
            value:{
                required:"该项必填"
            }
        },
        submitHandler:function () {

            $.ajax({
                url:"/foundPassword",
                type:"post",
                data:$("#foundForm").serialize(),
                beforeSend:function () {
                    $("#btn").append($("<i class='fa fa-spinner fa-spin'></i>")).attr("disabled","disabled");
                },
                success:function (data) {
                    if(data.state == "success") {
                        var type = $("#type").val();
                        if(type == "email") {
                            swal({
                                title: "发送成功",
                                text: "请到邮箱中查收激活邮件",
                                type: "success",
                            });
                            /*与注册时不同，页面没有跳转，用户可能多次提交发送邮件，需要做出限制*/
                            /*window.location.href = "/resetPassword";*/
                        } else {
                            //TODO
                        }
                    } else {
                        swal("Oops!", data.message, "error");
                    }
                },
                error:function () {
                    swal("Oops!", "服务器错误", "error");
                },
                complete:function () {
                    $("#btn").text("提交").removeAttr("disabled");
                }

            });
        }
    });

});
