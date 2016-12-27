$(function () {

    /*保持消息通知的实时性，轮询，间歇调用*/
    var loadNotify = function () {
        /*NotifyServlet已有get方法,$.post与$.get的格式查询jquery123*/
        $.post("/notify",function (result) {
            if(result.state == "success") {
                $("#unreadnum").text(result.data)
            }
        });
    };
    loadNotify();//运行
    setInterval(loadNotify,11*1000);
});
