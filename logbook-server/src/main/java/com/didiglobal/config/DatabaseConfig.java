package com.didiglobal.config;

import com.didiglobal.common.db.dbproxy.DbProxyDataSource;
import com.didiglobal.config.dbplugins.DbTraceLogProcessor;
import com.didiglobal.config.dbplugins.DbTraceProcessor;
import com.didiglobal.config.dbplugins.MybatisTraceInterceptor;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = {"com.didiglobal.mybatis.mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DatabaseConfig {

    @Autowired
    DatabaseProperties properties;

    @Autowired
    MybatisTraceInterceptor mybatisTraceInterceptor;

    // 给定 DataSource 生成 SqlSessionFactoryBean
    public SqlSessionFactory getSqlSessionFactory(DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPlugins(mybatisTraceInterceptor);

        SqlSessionFactory factory = sessionFactory.getObject();

        factory.getConfiguration().setMapUnderscoreToCamelCase(true);
        factory.getConfiguration().setCacheEnabled(false);

        return factory;
    }


    @Primary
    @Bean
    public DataSource dataSource() {
        try {
            return new DbProxyDataSource(BasicDataSourceFactory.createDataSource(properties));
        } catch (Exception e) {
            throw new IllegalStateException("create datasource failed", e.getCause());
        }
    }

    @Primary
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        return getSqlSessionFactory(dataSource);
    }


    @Component
    @ConfigurationProperties("spring.datasource")
    private class DatabaseProperties extends Properties {
    }


    /**
     * mybatis trace插件
     */
    @Bean
    public MybatisTraceInterceptor traceInterceptor(){
        List<DbTraceProcessor> processors = new ArrayList<>();
        processors.add(new DbTraceLogProcessor());
        return new MybatisTraceInterceptor(processors);
    }

}
