
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <button id="btn">load xml data</button>
    <table class="table">
        <thead>
            <tr>
                <th>ID</th>
                <th>NAME</th>
                <th>ADDRESS</th>
            </tr>
        </thead>

        <tbody>

        </tbody>

    </table>

    <script>
        (function(){
            document.querySelector("#btn").onclick = function () {

                var xmlHttp = new XMLHttpRequest();

                xmlHttp.open("get","/users.xml");

                xmlHttp.onreadystatechange = function () {
                    if(xmlHttp.readyState == 4) {
                        if(xmlHttp.status == 200) {
                            //获取服务端返回的XML文档，并进行解析
                            var xmlDoc = xmlHttp.responseXML;
                            alert(xmlDoc);
                            var users = xmlDoc.getElementsByTagName("user");
                            //var userElements = xmlDoc.getElementsByTagName("user");
                            var tbody = document.querySelector("tbody");

                            for(var i = 0;i < users.length;i++) {

                                var userElement = users[i];
                                var id = userElement.getAttribute("id");
                                var name = userElement.getElementsByTagName("name")[0].childNodes[0].nodeValue;
                                var address = userElement.getElementsByTagName("address")[0].childNodes[0].nodeValue;

                                var tr = document.createElement("tr");

                                var idTd = document.createElement("td");
                                var nameTd = document.createElement("td");
                                var addressTd = document.createElement("td");

                                var idNode = document.createTextNode(id);
                                var nameNode = document.createTextNode(name);
                                var addressNode = document.createTextNode(address);

                                idTd.appendChild(idNode);
                                nameTd.appendChild(nameNode);
                                addressTd.appendChild(addressNode);

                                tr.appendChild(idTd);
                                tr.appendChild(nameTd);
                                tr.appendChild(addressTd);

                                tbody.appendChild(tr);
                            }
                        } else {
                            alert("服务器异常" + xmlHttp.status);
                        }
                    }
                };

                xmlHttp.send();

            }
        })();
    </script>


</body>
</html>
