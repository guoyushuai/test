<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${requestScope.topic.title}</title>
    <link href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/bootstrap/2.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/js/editer/styles/simditor.css">

    <style>
        body {
            background-image: url(/static/img/bg.jpg);
        }

        .simditor .simditor-body {
            min-height: 100px;
        }
    </style>
</head>
<body>

<%@ include file="../include/navbar.jsp" %>

<!--header-bar end-->
<div class="container">
    <div class="box">
        <ul class="breadcrumb" style="background-color: #fff;margin-bottom: 0px;">
            <li><a href="/home">首页</a> <span class="divider">/</span></li>
            <li class="active">${requestScope.topic.node.nodename}</li>
        </ul>
        <div class="topic-head">
            <img class="img-rounded avatar"
                 src="http://oi0nv2rk1.bkt.clouddn.com/${requestScope.topic.user.avatar}?imageView2/1/w/60/h/60" alt="">
            <h3 class="title">${requestScope.topic.title}</h3>
            <p class="topic-msg muted"><a
                    href="">${requestScope.topic.user.username}</a> · <span id="topicTime">${requestScope.topic.createtime}</span></p>
        </div>
        <div class="topic-body">
            <p>${requestScope.topic.content}</p>
        </div>
        <div class="topic-toolbar">

            <c:if test="${not empty sessionScope.current_user}">
            <%--未登录不显示收藏、感谢、编辑按钮--%>
                <ul class="unstyled inline pull-left">

                    <c:choose>
                        <c:when test="${empty fav}">
                            <li><a href="javascript:;" id="favTopic" rel="fav">加入收藏</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="javascript:;" id="favTopic" rel="unfav">取消收藏</a></li>
                        </c:otherwise>
                    </c:choose>

                    <li><a href="">感谢</a></li>

                    <c:if test="${sessionScope.current_user.id == requestScope.topic.userid && requestScope.topic.edit}">
                        <%--没有登录且登录用户非发帖用户不显示编辑按钮，且仅发帖后十分钟内并没有回复可以修改--%>
                        <%--点击跳转到topicEdit,并把要编辑的帖子id传过去--%>
                        <li><a href="/topicEdit?topicid=${requestScope.topic.id}">编辑</a></li>
                    </c:if>

                </ul>
            </c:if>

            <ul class="unstyled inline pull-right muted">
                <li>${requestScope.topic.clicknum}次点击</li>
                <li><span id="favnum">${topic.favnum}</span>人收藏</li>
                <li>${requestScope.topic.thanksnum}人感谢</li>
            </ul>
        </div>
    </div>
    <!--box end-->

    <div class="box" style="margin-top:20px;">

        <c:if test="${not empty requestScope.replyList}">
            <%--没有回复不显示回复个数及最后回复时间--%>
            <div class="talk-item muted" style="font-size: 12px">
                    ${fn:length(requestScope.replyList)}个回复 | 直到<span id="lastreplytime">${requestScope.topic.lastreplytime}</span>
            </div>
        </c:if>

        <%--//forEach的属性varStatus 能获得迭代的属性 vs.Status第几次迭代--%>
        <c:forEach items="${requestScope.replyList}" var="reply" varStatus="vs">
            <%--${requestScope.reply.user.xxx}获取不到信息，在此reply非循环list中获得的reply--%>

            <%--锚标记，在其他楼层点击回复时带有该楼层链接的标签a href=#count时跳转到此标记所在的楼层--%>
            <a name="${vs.count}"></a>

            <div class="talk-item">
                <table class="talk-table">
                    <tr>
                        <td width="50">
                            <img class="avatar"
                                 src="http://oi0nv2rk1.bkt.clouddn.com/${reply.user.avatar}?imageView2/1/w/40/h/40"
                                 alt="">
                        </td>
                        <td width="auto">
                            <a href="" style="font-size: 12px">${reply.user.username}</a> <span style="font-size: 12px"
                                                                                class="replyTime">${reply.createtime}</span>
                            <br>
                            <p style="font-size: 14px">${reply.content}</p>
                        </td>
                        <td width="70" align="right" style="font-size: 12px">
                            <%--a标签可以任意设置一个rel属性(相当于其他标签的value属性) 值为vs.count(楼层数相当于给每一层一个唯一的id) --%>
                            <%--a标签的href=javascript:;不做跳转用--%>
                            <a href="javascript:;" rel="${vs.count}" class="replyLink" title="回复"><i class="fa fa-reply"></i></a>&nbsp;
                            <%--楼层数通过vs.count循环次数来判断--%>
                            <span class="badge">${vs.count}楼</span>
                        </td>
                    </tr>
                </table>
            </div>

        </c:forEach>

    </div>
    <%--锚标记--%><%--a标签，name属性，先标记再通过#使用.登录后通过该标记，页面直接跳转到该回复界面--%>
    <a name="reply"></a>
    <c:choose>
        <c:when test="${not empty sessionScope.current_user}">
            <div class="box" style="margin:20px 0px;">
                <div class="talk-item muted" style="font-size: 12px"><i class="fa fa-plus"></i> 添加一条新回复</div>
                <form id="replyForm" action="" style="padding: 15px;margin-bottom:0px;">
                    <input name="topicid" type="hidden" value="${requestScope.topic.id}">
                    <textarea name="content" id="editor"></textarea>
                </form>
                <div class="talk-item muted" style="text-align: right;font-size: 12px">
                    <span class="pull-left">请尽量让自己的回复能够对别人有帮助回复</span>
                    <button type="button" id="replyBtn" class="btn btn-primary">发布</button>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="box" style="margin:20px 0px;">
                <div class="talk-item muted" style="font-size: 24px">请<a href="/login?redirect=topicDetail?topicid=${requestScope.topic.id}#reply">登录</a>后再回复</div>
            </div>
        </c:otherwise>
    </c:choose>

</div>
<!--container end-->
<script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
<script src="/static/js/editer/scripts/module.min.js"></script>
<script src="/static/js/editer/scripts/hotkeys.min.js"></script>
<script src="/static/js/editer/scripts/uploader.min.js"></script>
<script src="/static/js/editer/scripts/simditor.min.js"></script>
<script src="/static/js/jquery.validate.min.js"></script>
<script src="/static/js/sweetalert.min.js"></script>
<script src="/static/js/moment/moment.js"></script>
<script src="/static/js/moment/zh-cn.js"></script>

<script>

    $(function () {
        <c:choose>
            <c:when test="${empty sessionScope.current_user}">
                $(".replyLink").click(function () {
                    window.location.href = "#reply";
                });
            </c:when>
            <c:otherwise>
                /*未登录时屏蔽了回复功能，同时应该屏蔽编辑框，避免加载编辑框时报错*/
                var editor;
                editor = new Simditor({
                    textarea: $('#editor')
                    //optional options
                });

                //getValue(),setValue()分别获取与设置content中的值
                $(".replyLink").click(function () {
                    //simditor中$("#editor").text()获取不到值也设置不了值
                    //editor定义为simditor对象，用simditor中的方法，(查看文档)
                    //获取到当前楼层的rel属性的值，定义时赋值为楼层
                    var count = $(this).attr("rel");

                    /*为回复内容添加显示为count的值的锚标记，点击该标记时定位到含a标签name值为count值的楼层*/
                    /*<a href="#count">count</a>*/
                    var html = "<a href='#" + count +"'>@"+count+"</a>"
                    //获取回复框已输入的值，防止后添加回复楼层时，已输入内容被清空
                    editor.setValue(html + editor.getValue());

                    //点击楼层中的回复按钮后跳转到回复框内，同登录后再回复指向含a标签name属性值为reply的锚标记的回复框一样
                    window.location.href = "#reply";
                });




                $("#favTopic").click(function () {
                    var action = "";
                    if($(this).text() == "加入收藏") {
                        alert("未收藏需加入");
                        action = "fav";
                    } else if($(this).text() == "取消收藏") {
                        alert("已收藏需取消");
                        action = "unfav";
                    };
                    //需要给客户端传递一个参数，来判断用户是否点击了收藏按钮

                    //乱！绕晕了，放弃治疗
                    /*var action = "";
                     if ($(this).attr("rel") == "fav") {
                     alert("未收藏需加入");
                     action = "fav";
                     } else if($(this).attr("rel") == "unfav") {
                     alert("已收藏需取消");
                     action = "unfav";
                     }*/

                    /*$(this)获取不到当前的控件了*/
                    var $this = $(this);
                    $.post("/topicFav",{"topicid":${requestScope.topic.id},"action":action})
                        .done(function (result) {
                            if(result.state == "success") {
                                alert("服务端已更新");
                                if(action == "fav") {
                                    alert("客户端加入，字面变取消，人数+1");
                                    /*$(this).html("取消收藏");*/
                                    $this.html("取消收藏");
                                } else if(action == "unfav") {
                                    alert("客户端取消，字面变加入，人数-1");
                                    /*$(this).html("加入收藏");*/
                                    $this.html("加入收藏");
                                }
                                //修改页面上的收藏次数
                                $("#favnum").text(result.data);
                            } else {
                                swal("操作失败!",result.message, "error");
                            }
                        }).error(function () {
                        swal("Oops!","服务器异常", "error");
                    });
                });
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty sessionScope.current_user}">


        </c:if>

        $("#replyBtn").click(function () {
            $("#replyForm").submit();
        });

        //先获取到传递过来的值，通过moment的fromnow转化成距当前时间。再赋值给相应的标签
        $("#topicTime").text(moment($("#topicTime").text()).fromNow());

        //先获取到传递过来的值，通过moment的format格式化，再赋值给相应的标签
        $("#lastreplytime").text(moment($("#lastreplytime").text()).format("YYYY年MM月DD日 hh:mm:ss"));

        //不能用id选择器，回复列表循环出多个回复，用id选择器，后面转化后的时间会覆盖回复中的所有需要格式化的时间
        //jquery123中的text()方法，可以传入String，也可以传入function
        $(".replyTime").text(function () {
            return moment($(this).text()).fromNow();
            /*var replyTime = $(this).text();
            return moment(replyTime).fromNow();*/
        });


        //validate不校验隐藏域
        $.extend($.validator.defaults,{ignore:""});

        $("#replyForm").validate({
            errorElement:"span",
            errorclass:"text-error",
            rules:{
                content:{
                    required:true
                }
            },
            messages:{
                content:{
                    required:"回复不能为空"
                }
            },
            submitHandler:function (form) {
                $.ajax({
                    url:"/newReply",
                    type:"post",
                    data:$(form).serialize(),
                    beforeSend:function () {
                        $("#replyBtn").append($("<i class='fa fa-spinner fa-spin'></i>")).attr("disabled", "disabled");
                    },
                    success:function (result) {
                        if(result.state == "success") {
                            //等于刷新页面servlet只传了topicid这个参数进入topicDetail的doget方法根据topicid查找了帖子详情再请求转发到相应jsp
                            //暂时回复后定位到回复框，不能定位到自己刚回复的楼层
                            window.location.href = "/topicDetail?topicid=" + result.data;
                        } else {
                            swal("Oops!",result.message, "error");
                           /* window.location.href = "/home";*/
                            /*if(result.message == "帖子不存在或已被删除") {
                                swal("Oops!",result.message, "error");
                                window.location.href = "/home";
                            } else if(result.message == "回复不存在或已被删除") {
                                swal("Oops!","回复失败", "error");
                                window.location.href = "/topicDetail?topicid=" + result.date;
                            }*/
                        }
                    },
                    error:function () {
                        swal("Oops!","服务器异常", "error");
                    },
                    complete:function () {
                        $("#replyBtn").html("回复").removeAttr("disabled");
                    }
                })
            }
        })

    });;
</script>

</body>
</html>
