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
public class SpringBootServerlessApplication
        extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootServerlessApplication.class, args);
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

}
