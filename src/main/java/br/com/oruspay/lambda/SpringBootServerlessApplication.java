package br.com.oruspay.lambda;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.spi.ApplicationContextRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * Spring App Config without SpringBoot
 * @author Ricardo M. Augusto
 *
 */
@Configuration
@EnableWebMvc
public class SpringBootServerlessApplication {

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Create registry to integrate spring bean with Apache Context
	 * @return
	 * @throws Exception
	 */
    @Bean
    public ApplicationContextRegistry createRegistry() throws Exception {
    	return new ApplicationContextRegistry(applicationContext);
	}	
	
    /**
     * Start CamelContext
     * @return
     * @throws Exception
     */
    @Bean
    public CamelContext camel() throws Exception {
        CamelContext camelContext = new DefaultCamelContext(createRegistry());
        camelContext.addRoutes(appRouterConfig());
        camelContext.start();
        return camelContext;
    }
	
	@Bean
    public ProducerTemplate producerTemplate() throws Exception{   
        return camel().createProducerTemplate();
    }

    @Bean
    RoutesBuilder appRouterConfig() {
        return new RouteConfig();
    }

    @Bean
    public CamelServletWrapper proxyController() {
        return new CamelServletWrapper();
    }

    @Bean
    public HandlerMapping handlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        ((Map<String, Object>) mapping.getUrlMap()).put("/**",
                proxyController());
        mapping.setOrder(1);
        return mapping;

    }

    @Bean
    public HandlerAdapter handlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new HandlerExceptionResolver() {

            @Override
            public ModelAndView resolveException(HttpServletRequest request,
                    HttpServletResponse response, Object handler,
                    Exception ex) {
                return null;
            }
        };
    }
}