package br.com.oruspay.lambda;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.FatJarPackageScanClassResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

@SpringBootApplication
public class SpringBootServerlessApplication  extends SpringBootServletInitializer  {

    //SpringBootServletInitializer
    //WebApplicationInitializer
    
    @Autowired
    CamelContext context;
    
    public static void main(String[] args) {
        
        System.setProperty("logging.level.root", "DEBUG");
        SpringApplication.run(SpringBootServerlessApplication.class, args);
        
    }
    
//    @Bean()
//    @Order(value=1)
//    public FatJarPackageScanClassResolver fatJar() {
//        return new FatJarPackageScanClassResolver();
//    }
    
    @Bean
    public ProxyController proxyController() {
      return new ProxyController();
    }
    

    @Bean
    public HandlerMapping handlerMapping() {
        
        System.out.println("criou o handlerMapping");
        
      SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
      ((Map<String,Object>)mapping.getUrlMap()).put("/**", proxyController());
      mapping.setOrder(1);
      return mapping;
    }

//    @Bean
//    ServletRegistrationBean camelServlet() {
//        ServletRegistrationBean mapping = new ServletRegistrationBean();
//        mapping.setName("CamelServlet");        
//        mapping.setLoadOnStartup(10);
//        mapping.setServlet(new CamelHttpTransportServlet());
//        mapping.addUrlMappings("/*");        
//        return mapping;
//    }
    
//    @Bean
//    public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration() {
//        DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean("springForwardCamelFilter");
//        registration.setInitParameters(new HashMap<>());
//        registration.setOrder(1);
//        return registration;
//    }    
         
//    @Bean
//    public FilterRegistrationBean someFilterRegistration() {
//
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new SpringForwardCamelFilter());
//        registration.addUrlPatterns("/*");
//        registration.setName("SpringForwardCamelFilter");
//        registration.setOrder(1);
//        return registration;
//    }  
    
    
    
    
}
