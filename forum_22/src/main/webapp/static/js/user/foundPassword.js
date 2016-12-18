$(function () {

    $("#type").change(function () {
        var option = $(this).val();
        if("email" == option) {
            $("#typename").text("电子邮件");
        } else if("phone" == option) {
            $("#typename").text("手机号码");
        }
    });

    $("#btn").click(function () {
        $("#foundPasswordForm").submit();
    });

    $("#foundPasswordForm").validate({
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
                data:$("#foundPasswordForm").serialize(),
                beforeSend:function () {
                    $("#btn").append($("<i class='fa fa-spinner fa-spin'></i>")).attr("disabled","disabled");
                },
                success:function (data) {
                    if(data.state == "success") {
                        var type = $("#type").val();
                        if(type == "email") {
                            alert("请到邮箱中查收邮件做下一步操作");
                            /*与注册时不同，页面没有跳转，用户可能多次提交发送邮件，需要做出限制*/
                            /*window.location.href = "/resetPassword";*/
                        } else {
                            //TODO
                        }
                    } else {
                        alert(data.message)
                    }
                },
                error:function () {
                    alert("服务器错误");
                },
                complete:function () {
                    $("#btn").text("提交").removeAttr("disabled");
                }

            });
        }
    });

});
