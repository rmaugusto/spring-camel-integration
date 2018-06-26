package br.com.oruspay.lambda;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RouteConfig  extends RouteBuilder {

    public void configure() {

        restConfiguration()
        .enableCORS(true)
        .component("servlet")
        .dataFormatProperty("prettyPrint", "true");        

        
        
//      rest("servlet:///hello?servletName=dispatcherServlet").get().to("direct:hello");
      
        from("servlet:/api").to("direct:hello");
         
        rest("/").get().to("direct:hello");
        from("direct:hello").transform().simple("Hello World!");

    }
    
}
