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

        from("servlet:/servlet").to("direct:hello");
        rest("/api/gravar/rest").get().to("direct:hello").produces("application/json") ;
        from("direct:hello").transform().simple("Hello World!");

    }
    
}
