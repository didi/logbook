package com.didiglobal.config;

import com.didiglobal.common.authz.filter.ApiCallerFilter;
import com.didiglobal.common.authz.filter.AuthorizationProcessor;
import com.didiglobal.common.authz.filter.CallerConfigProvider;
import com.didiglobal.common.authz.filter.Md5AuthorizationProcessor;
import com.didiglobal.common.authz.resolver.ApiCallerResolver;
import com.didiglobal.common.message.MessageManager;
import com.didiglobal.common.message.RestfulMessageConverter;
import com.didiglobal.common.trace.filter.TraceLogFilter;
import com.didiglobal.common.web.DelegatingFilterFactoryBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.List;
import java.util.Map;



@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {



    @Resource
    CallerConfigProvider callerConfigProvider;


    // objectMapper bean
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public MessageManager messageManager() {
        return MessageManager.INSTANCE;
    }

    // RestfulMessageConverter 用来做文案映射。
    // 对应的配置文件是 message-zh_CN.properties
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new RestfulMessageConverter(objectMapper(), messageManager());
    }

    @Bean
    public AuthorizationProcessor authorizationProcessor() {
        return new Md5AuthorizationProcessor();
    }

    public ApiCallerFilter callerFilter() {
        return ApiCallerFilter.custom(callerConfigProvider)
                .setAuthorizationProcessor(authorizationProcessor())
                .setMessageManager(MessageManager.INSTANCE)
                .build();
    }

    @Bean("delegatingFilterFactoryBean")
    public DelegatingFilterFactoryBean delegatingFilterFactoryBean() {
        DelegatingFilterFactoryBean factory = new DelegatingFilterFactoryBean();

        Map<String, Filter> filterMap = factory.getFilters();
        filterMap.put("trace", new TraceLogFilter());
        filterMap.put("apiCallerAuth", callerFilter());

        Map<String, String> definitionMap = factory.getFilterChainDefinitionMap();
        definitionMap.put("/alive/status", "trace,apiCallerAuth");
        definitionMap.put("/**", "trace,apiCallerAuth");

        return factory;
    }


    // 这个用来配置 resolver
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ApiCallerResolver());
    }



    /**
     * 解决日志 errmsg 中文乱码问题
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true)
                .parameterName("mediaType")
                .ignoreAcceptHeader(true)
                .defaultContentType(MediaType.APPLICATION_JSON_UTF8)
                .mediaType("json", MediaType.APPLICATION_JSON_UTF8);
    }


}
