package com.gys.web.filter;

import com.gys.exception.ServiceException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

        System.out.println("获取uri:" + uri);

        //不能用StringUtils.isEmaty,因为urlList可能为空
        if(urlList != null && urlList.contains(uri)){
            //非登陆用户不能访问的页面需要被过滤
            if(request.getSession().getAttribute("current_user") != null) {
                //已登录用户直接通过
                filterChain.doFilter(request,response);
            } else {

                //未登录用户跳转到登录页面，并获取跳回的参数
                Map map = request.getParameterMap();
                Set set = map.entrySet();
                Iterator iterator = set.iterator();
                /*Iterator iterator = request.getParameterMap().entrySet().iterator();*/
                if(iterator.hasNext()) {
                    uri += "?";

                    while(iterator.hasNext()) {
                        //不确定map中存放的是什么东西的时候，用键值对视图map.entry代替键值对对象
                        Map.Entry entry = (Map.Entry) iterator.next();
                        Object key = entry.getKey();
                        Object value = entry.getValue();

                        //值为字符串数组
                        String valString[] = (String[]) value;
                        String param = "";
                        for(int i = 0;i < valString.length;i++) {
                            param = key + "=" + valString[i] + "&";
                            uri += param;
                        }
                    }
                    //把拼接好的uri最后的那个$去掉
                    uri = uri.substring(0,uri.length()-1);
                    System.out.println("拼接uri = " + uri);
                }

                //未登录用户
                response.sendRedirect("/login?redirect=" + uri);
            }
        } else {
            filterChain.doFilter(request,response);
        }

    }
}
