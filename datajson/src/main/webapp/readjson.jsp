
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<button id="btn">load json</button>
<script>
    (function(){
        document.querySelector("#btn").onclick = function(){

            var xmlHttp = new XMLHttpRequest();

            xmlHttp.open("get","/data.json");

            xmlHttp.onreadystatechange = function(){
                if(xmlHttp.readyState == 4) {
                    if(xmlHttp.status == 200) {

                        //获取服务器返回的字符串
                        var result = xmlHttp.responseText;
                        //将字符串转换为JSON
                        var json = JSON.parse(result);

                        for(var i = 0;i < json.length;i++) {
                            var user = json[i];
                            alert(user.id + "->" + user.name + "->" + user.address);
                        }

                    }
                }

            }

            xmlHttp.send();

        }
    })();
</script>

</body>
</html>
