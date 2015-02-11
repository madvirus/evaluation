package net.madvirus.eval.web;

import net.madvirus.eval.web.restapi.ExceptionHandlerAdvice;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;

public abstract class MockMvcUtil {
    public static MockMvc mockMvc(Object controller) {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
                    HandlerMethod handlerMethod, Exception exception) {
                // 익셉션을 ExceptionHandlerAdvice가 처리하도록 설정
                Method method = new ExceptionHandlerMethodResolver(
                        ExceptionHandlerAdvice.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new ExceptionHandlerAdvice(), method);
            }
        };
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();

        return MockMvcBuilders.standaloneSetup(controller)
                .setHandlerExceptionResolvers(exceptionResolver).build();
    }
}
