package br.com.oruspay.lambda;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletSecurityElement;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.AwsProxyExceptionHandler;
import com.amazonaws.serverless.proxy.AwsProxySecurityContextWriter;
import com.amazonaws.serverless.proxy.ExceptionHandler;
import com.amazonaws.serverless.proxy.RequestReader;
import com.amazonaws.serverless.proxy.ResponseWriter;
import com.amazonaws.serverless.proxy.SecurityContextWriter;
import com.amazonaws.serverless.proxy.internal.servlet.AwsHttpServletResponse;
import com.amazonaws.serverless.proxy.internal.servlet.AwsProxyHttpServletRequest;
import com.amazonaws.serverless.proxy.internal.servlet.AwsProxyHttpServletRequestReader;
import com.amazonaws.serverless.proxy.internal.servlet.AwsProxyHttpServletResponseWriter;
import com.amazonaws.serverless.proxy.internal.servlet.AwsServletContext;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;

public class OpSpringBootLambdaContainerHandler<RequestType, ResponseType>
        extends SpringBootLambdaContainerHandler<RequestType, ResponseType> {

    public OpSpringBootLambdaContainerHandler(
            Class<RequestType> requestTypeClass,
            Class<ResponseType> responseTypeClass,
            RequestReader<RequestType, AwsProxyHttpServletRequest> requestReader,
            ResponseWriter<AwsHttpServletResponse, ResponseType> responseWriter,
            SecurityContextWriter<RequestType> securityContextWriter,
            ExceptionHandler<ResponseType> exceptionHandler,
            Class<? extends WebApplicationInitializer> springBootInitializer)
            throws ContainerInitializationException {
        super(requestTypeClass, responseTypeClass, requestReader,
                responseWriter, securityContextWriter, exceptionHandler,
                springBootInitializer);
    }
    
    /**
     * Creates a default SpringLambdaContainerHandler initialized with the `AwsProxyRequest` and `AwsProxyResponse` objects and the given Spring profiles
     * @param springBootInitializer {@code SpringBootServletInitializer} class
     * @param profiles A list of Spring profiles to activate
     * @return An initialized instance of the `SpringLambdaContainerHandler`
     * @throws ContainerInitializationException If an error occurs while initializing the Spring framework
     */
    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> getAwsProxyHandler(Class<? extends WebApplicationInitializer> springBootInitializer, String... profiles)
            throws ContainerInitializationException {
        
        @SuppressWarnings("unchecked")
        SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> newHandler = new SpringBootLambdaContainerHandler(
                AwsProxyRequest.class,
                AwsProxyResponse.class,
                new AwsProxyHttpServletRequestReader(),
                new AwsProxyHttpServletResponseWriter(),
                new AwsProxySecurityContextWriter(),
                new AwsProxyExceptionHandler(),
                springBootInitializer);
        
        newHandler.activateSpringProfiles(profiles);
        newHandler.initialize();
        
        return newHandler;
    }    
    
    
    private class SpringBootAwsServletContext extends AwsServletContext {
        private static final String DISPATCHER_SERVLET_NAME = "dispatcherServlet";

        public SpringBootAwsServletContext() {
            super(OpSpringBootLambdaContainerHandler.this);
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String s, String s1) {
            throw new UnsupportedOperationException(
                    "Only " + DISPATCHER_SERVLET_NAME + " is supported");
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String s,
                Class<? extends Servlet> aClass) {
            throw new UnsupportedOperationException(
                    "Only " + DISPATCHER_SERVLET_NAME + " is supported");
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String s,
                Servlet servlet) {
            if (DISPATCHER_SERVLET_NAME.equals(s)) {
                try {
                    servlet.init(new ServletConfig() {
                        @Override
                        public String getServletName() {
                            return s;
                        }

                        @Override
                        public ServletContext getServletContext() {
                            return SpringBootAwsServletContext.this;
                        }

                        @Override
                        public String getInitParameter(String name) {
                            return null;
                        }

                        @Override
                        public Enumeration<String> getInitParameterNames() {
                            return new Enumeration<String>() {
                                @Override
                                public boolean hasMoreElements() {
                                    return false;
                                }

                                @Override
                                public String nextElement() {
                                    return null;
                                }
                            };
                        }
                    });
                } catch (ServletException e) {
                    throw new RuntimeException("Cannot add servlet " + servlet,
                            e);
                }
                return new ServletRegistration.Dynamic() {
                    @Override
                    public String getName() {
                        return s;
                    }

                    @Override
                    public String getClassName() {
                        return null;
                    }

                    @Override
                    public boolean setInitParameter(String name, String value) {
                        return false;
                    }

                    @Override
                    public String getInitParameter(String name) {
                        return null;
                    }

                    @Override
                    public Set<String> setInitParameters(
                            Map<String, String> initParameters) {
                        return null;
                    }

                    @Override
                    public Map<String, String> getInitParameters() {
                        return null;
                    }

                    @Override
                    public Set<String> addMapping(String... urlPatterns) {
                        return null;
                    }

                    @Override
                    public Collection<String> getMappings() {
                        return null;
                    }

                    @Override
                    public String getRunAsRole() {
                        return null;
                    }

                    @Override
                    public void setAsyncSupported(boolean isAsyncSupported) {

                    }

                    @Override
                    public void setLoadOnStartup(int loadOnStartup) {

                    }

                    @Override
                    public Set<String> setServletSecurity(
                            ServletSecurityElement constraint) {
                        return null;
                    }

                    @Override
                    public void setMultipartConfig(
                            MultipartConfigElement multipartConfig) {

                    }

                    @Override
                    public void setRunAsRole(String roleName) {

                    }
                };
            } else {
                throw new UnsupportedOperationException(
                        "Only " + DISPATCHER_SERVLET_NAME + " is supported");
            }
        }
    }

}
