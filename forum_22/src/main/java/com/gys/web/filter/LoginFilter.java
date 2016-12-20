package com.gys.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LoginFilter extends AbstractFilter {

    private List<String> urlList = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        //获取web.xml中设值的需要被过滤的url字符串（name为validateUrl的value的值），并将其以,为分隔符转换为字符串
        String validateUrl = filterConfig.getInitParameter("validateUrl");
        urlList = Arrays.asList(validateUrl.split(","));

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //将servletRequest和servletResponse强制转换为对应的Http子接口
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取用户请求的url
        String uri = request.getRequestURI();

        //不能用StringUtils.isEmaty,因为urlList可能为空
        if(urlList != null && urlList.contains(uri)){
            //非登陆用户不能访问的页面需要被过滤
            if(request.getSession().getAttribute("current_user") != null) {
                filterChain.doFilter(request,response);
            } else {
                response.sendRedirect("/login?redirect=" + uri);
            }
        } else {
            filterChain.doFilter(request,response);
        }

    }
}
