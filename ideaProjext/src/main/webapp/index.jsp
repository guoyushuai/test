
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<%--<form action="" enctype="application/x-www-form-urlencoded"></form>--%>
<input type="text" id="name">
<button id="btn">sendRequest</button>
<div id="result"></div>

<script>
    (function () {

        function createXmlHttp() {
            var xmlHttp = null;
            if(window.ActiveXObject) {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            } else {
                xmlHttp = new XMLHttpRequest();
            }
            return xmlHttp;
        }

        document.querySelector("#btn").onclick = function () {
            var name = document.querySelector("#name").value;
            //sendGet(name);
            sendPost(name);
        }

        function sendGet(name) {
            //获取ajax引擎
            var xmlHttp = createXmlHttp();
            //指定请求方式
            xmlHttp.open("get","/ajax?name=" + name + "&_=" + new Date().getTime());//避免get缓存
            //发出请求
            xmlHttp.send();
        }

        function sendPost(name) {
            //获取Ajax引擎
            var xmlHttp = createXmlHttp();
            //指定请求方式和地址，并设置请求头
            xmlHttp.open("post","/ajax");
            xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            //配置回调函数
            xmlHttp.onreadystatechange = function() {
                var state = xmlHttp.readyState;//服务器状态码
                if(state == 4) {
                    var httpState = xmlHttp.status;//Http状态码
                    if(httpState == 200) {
                        //获取服务端返回的字符串
                        var result = xmlHttp.responseText;
                        var div = document.querySelector("#result");
                        if(result == "可用") {
                            div.innerText = "可以使用";
                        } else {
                            div.innerText = "已被占用";
                        }
                    } else {
                        alert("服务器错误" + httpState);
                    }
                }
            }
            //发出请求
            xmlHttp.send("name=" + name);
        }
    })();
</script>

</body>
</html>
