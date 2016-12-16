package com.gys.web.filter;

import com.gys.util.StringUtil;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter extends AbstractFilter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String encode = filterConfig.getInitParameter("encoding");

        if (StringUtil.isNotEmpty(encode)) {
            this.encoding = encode;
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(encoding);
        servletResponse.setCharacterEncoding(encoding);
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
