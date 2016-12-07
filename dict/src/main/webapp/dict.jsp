
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <input type="text" id="keyword">
    <button id="btn">query</button>
    <%--<div id="result"></div>--%>
    <p id="result"></p>
    <script>
        (function(){
            document.querySelector("#btn").onclick = function() {
                var keyword = document.querySelector("#keyword").value;
                var xmlHttp = new XMLHttpRequest();
                xmlHttp.open("get","/dict?query=" + keyword);
                xmlHttp.onreadystatechange = function() {
                    if(xmlHttp.readyState == 4) {
                        if(xmlHttp.status == 200) {

                            var xmlDoc = xmlHttp.responseXML;
                            alert(xmlDoc);
                            var errorCode = xmlDoc.getElementsByTagName("errorCode")[0].childNodes[0].nodeValue;
                            if(errorCode == 0) {
                                var ex = xmlDoc.getElementsByTagName("ex")[0].childNodes[0].nodeValue;

                                document.querySelector("#result").innerText = ex;
                                /*var div = document.querySelector("#result");
                                div.innerText = ex;*/

                            } else {

                                alert("ERRORCODE:" + errorCode);

                            }
                        } else {
                            alert("服务器异常" + xmlHttp.status);
                        }
                    }
                }

                xmlHttp.send();

            }
        })();
    </script>

</body>
</html>
