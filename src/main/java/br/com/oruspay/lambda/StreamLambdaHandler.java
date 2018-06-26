package br.com.oruspay.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class StreamLambdaHandler implements RequestStreamHandler {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            
            System.out.println("vai inicializar SpringBootLambdaContainerHandler");
            handler = SpringBootLambdaContainerHandler
                    .getAwsProxyHandler(SpringBootServerlessApplication.class);
/*
            WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(handler.getServletContext());
            
            System.out.println("wc " + wc);
            
            ProxyController pc = wc.getBean(ProxyController.class);
            
            System.out.println("pc " + pc);
            
            handler.onStartup(c -> {
                System.out.println("Configurado filter springForwardCamelFilter");
                c.addFilter("springForwardCamelFilter", SpringForwardCamelFilter.class);
            });
*/            
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold
            // start
            e.printStackTrace();
            throw new RuntimeException(
                    "Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream,
            OutputStream outputStream, Context context) throws IOException {
        
        handler.proxyStream(inputStream, outputStream, context);

        // just in case it wasn't closed by the mapper
        outputStream.close();
    }
}
