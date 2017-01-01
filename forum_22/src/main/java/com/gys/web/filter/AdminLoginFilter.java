package com.gys.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdminLoginFilter extends AbstractFilter {

    private List<String> adminUrlList = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String validateAdminUrl = filterConfig.getInitParameter("validateAdminUrl");
        adminUrlList = Arrays.asList(validateAdminUrl.split(","));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();

        if(uri.startsWith("/admin")) {
            if(adminUrlList != null && adminUrlList.contains(uri)) {
                if(request.getSession().getAttribute("current_admin") == null) {
                    response.sendRedirect("/admin/login?redirect=" + uri);
                } else {
                    filterChain.doFilter(request,response);
                }
            } else {
                filterChain.doFilter(request,response);
            }
        }
    }
}
