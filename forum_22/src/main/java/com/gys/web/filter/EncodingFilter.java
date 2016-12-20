package com.gys.web.filter;

import com.gys.util.StringUtil;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter extends AbstractFilter {

    //设置默认值
    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        //获取xml中name为encoding的value值
        String encode = filterConfig.getInitParameter("encoding");

        //判断xml中是否设置该值，若设置则替换默认值，没设置将使用默认值
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
