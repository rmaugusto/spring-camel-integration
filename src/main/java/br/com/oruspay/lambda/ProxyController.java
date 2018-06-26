package br.com.oruspay.lambda;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ServletWrappingController;

public class ProxyController extends ServletWrappingController implements InitializingBean {

    public ProxyController() {
      setServletClass(CamelHttpTransportServlet.class);
      setServletName("CamelServlet");
      setSupportedMethods((String[]) null);
//      Properties properties = new Properties();
//      setInitParameters(properties);
      
      System.out.println("criou o ProxyController");
      
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
        System.out.println("Vai processar handleRequestInternal");
        return super.handleRequestInternal(new PathStripper(request), response);
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
            System.out.println("getPathInfo "+value);
            return value;
        }

    }

}
