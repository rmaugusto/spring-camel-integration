package br.com.oruspay.lambda;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component("springForwardCamelFilter")
public class SpringForwardCamelFilter implements Filter {

    // @Autowired
    // ServletContext servletContext;
    //
    @Autowired
    private ServletConfig config;
    
    private FilterConfig filterConfig;

    private CamelHttpTransportServlet camelServlet = new CamelHttpTransportServlet();
    private boolean initialized = false;

    @Override
    public void init(FilterConfig fc) throws ServletException {
        
        if(config == null) {
            config = new ServletConfigAdapter(fc);
        }
        
        camelServlet.init(config);
        initialized = true;
        this.filterConfig = fc;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if(config == null) {
            config = new ServletConfigAdapter(this.filterConfig);
        }
        
        if (!initialized) {
            camelServlet.init(config);
            initialized = true;
        }

        camelServlet.service(new PathStripper((HttpServletRequest) request),
                response);

        // chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        camelServlet.destroy();
    }

    private class ServletConfigAdapter implements ServletConfig {

        private FilterConfig filterConfig;

        public ServletConfigAdapter(FilterConfig filterConfig) {
            this.filterConfig = filterConfig;
        }

        @Override
        public String getServletName() {
            return "CamelServlet";
        }

        @Override
        public ServletContext getServletContext() {
            return filterConfig.getServletContext();
        }

        @Override
        public String getInitParameter(String name) {
            return filterConfig.getServletContext().getInitParameter(name);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return filterConfig.getServletContext().getInitParameterNames();
        }

    }

    private static class PathStripper extends HttpServletRequestWrapper {

        public PathStripper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getPathInfo() {
            String value = super.getRequestURI();
            int index = value.indexOf("?");
            if (index > 0) {
                value = value.substring(0, index);
            }
            while (value.startsWith("/")) {
                value = value.substring(1);
            }
            System.out.println("getPathInfo " + value);
            return value;
        }

    }

}
