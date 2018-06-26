package br.com.oruspay.lambda;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ServletWrappingController;

/**
 * Essa classe é responsável por intermediar o CamelHttpTransportServlet do
 * dispatcherServlet do Spring Dessa forma as duas convivem simultaneamente nos
 * mapeamentos de URL
 * 
 * @author ricardo
 *
 */
public class CamelServletWrapper extends ServletWrappingController
        implements InitializingBean {

    public CamelServletWrapper() {
        setServletClass(CamelHttpTransportServlet.class);
        setServletName("CamelServlet");
        setSupportedMethods((String[]) null);
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return super.handleRequestInternal(new PathStripper(request), response);
    }

    /**
     * Essa classe é responsável por resolver a URL e alimentar o pathInfo
     * necessário no CamelHttpTransportServlet
     * 
     * @author ricardo
     *
     */
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
            return value;
        }

    }

}
