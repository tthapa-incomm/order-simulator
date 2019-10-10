package com.incomm.vms.tools.ordersimulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class OrderSimulatorApplication {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUserName;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Bean
    public Backend db() {
        return new Backend(jdbcTemplate);
    }

    @Bean
    public Service service() {
        return new Service(db());
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(datasourceUrl);
        dataSourceBuilder.username(datasourceUserName);
        dataSourceBuilder.password(datasourcePassword);
        return dataSourceBuilder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderSimulatorApplication.class, args);
    }

}
