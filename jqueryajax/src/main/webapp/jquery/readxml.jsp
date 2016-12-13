
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

    <input type="text" placeholder="RSS URL" id="url">

    <button id="btn">load rss</button>
    <ul id="newsList"></ul>
    <script src="/static/js/jquery-1.12.4.min.js"></script>
    <script>
        $(function(){
            $("#btn").click(function () {

                var rssUrl = $("#url").val();

                $.ajax({
                    url : "/rss.xml",
                    type : "get",

                    data : {"url":rssUrl},

                    success : function (xmlDoc) {

                        $(xmlDoc).find("item").each(function () {
                            var title = $(this).find("title").text();
                            var link = $(this).find("link").text();
                            $("<li><a href='"+ link +"'>" + title +"</a></li>").appendTo($("#newsList"));
                        });

                    },
                    error : function () {
                        alert("服务器异常");
                    }
                })
            })
        });
    </script>

</body>
</html>
