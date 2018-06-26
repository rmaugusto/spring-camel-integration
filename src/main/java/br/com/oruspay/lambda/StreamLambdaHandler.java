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

/**
 * Classe responsável por inicializar o Spring quando rodado na AWS e tratar as
 * mensagens do Lambda. O handler é inicializado de forma estática afim de
 * manter o contexto sempre em memória até a destruição do container pelo
 * lambda.
 * 
 * @author ricardo
 *
 */
public class StreamLambdaHandler implements RequestStreamHandler {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler
                    .getAwsProxyHandler(SpringBootServerlessApplication.class);
        } catch (ContainerInitializationException e) {
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
