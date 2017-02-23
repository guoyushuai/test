<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- search form -->
        <%--<form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search...">
                <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
            </div>
        </form>--%>
        <!-- /.search form -->
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">

            <shiro:hasRole name="role_admin">
            <li class="header">设置模块</li>
            <%--当前节点及子节点高亮显示--%>
            <li class="treeview ${fn:startsWith(param.menu,'sys_') ? 'active' : ''} ">
                <a href="#">
                    <i class="fa fa-cogs"></i> <span>系统设置</span> <i class="fa fa-angle-left pull-right"></i>
                </a>
                <%--系统设置模块子模块--%>
                <ul class="treeview-menu">
                    <li class="${param.menu == 'sys_accounts' ? 'active' : ''}">
                        <a href="/setting/user"><i class="fa fa-circle-o"></i>账户管理</a>
                    </li>
                    <li class="${param.menu == 'sys_device' ? 'active' : ''}">
                        <a href="/setting/device"><i class="fa fa-circle-o"></i>设备管理</a>
                    </li>
                    <li class="${param.menu == 'sys_labor' ? 'active' : ''}">
                    <a href="/setting/labor"><i class="fa fa-circle-o"></i>劳务管理</a>
                    </li>
                </ul>
            </li>
            </shiro:hasRole>

            <li class="header">业务模块</li>
            <li class="treeview ${param.menu == 'business_device_rent' ? 'active' : ''}">
                <a href="/device/rent">
                    <i class="fa fa-circle-o"></i> <span>设备租赁</span>
                </a>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-circle-o"></i> <span>劳务派遣</span>
                </a>
            </li>
            <li class="treeview ${fn:startsWith(param.menu,'pan') ? 'active' : ''}">
                <a href="/pan">
                    <i class="fa fa-circle-o"></i> <span>网盘系统</span>
                </a>
            </li>

            <shiro:hasRole name="role_fin">
            <li class="header">财务模块</li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-circle-o"></i> <span>财务报表</span>
                </a>
            </li>
            </shiro:hasRole>

            <li class="header">用户模块</li>
            <li class="treeview">
                <a href="/logout">
                    <i class="fa fa-sign-out"></i> <span>安全退出</span>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>
