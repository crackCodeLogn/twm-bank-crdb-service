package com.vv.personal.twm.crdb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableFeignClients
@EnableEurekaClient
@ComponentScan({"com.vv.personal.twm.crdb", "com.vv.personal.twm.ping"})
@SpringBootApplication
public class BankCrDbServer {
    public static void main(String[] args) {
        SpringApplication.run(BankCrDbServer.class, args);
    }

    @Autowired
    private Environment environment;

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vv.personal.twm.crdb"))
                .build();
    }

    private static final String LOCALHOST = "localhost";
    private static final String LOCAL_SPRING_PORT = "server.port";
    private static final String SWAGGER_UI_URL = "http://%s:%s/swagger-ui/index.html";

    @EventListener(ApplicationReadyEvent.class)
    public void firedUpAllCylinders() {
        String host = LOCALHOST;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Failed to obtain ip address. ", e);
        }
        String port = environment.getProperty(LOCAL_SPRING_PORT);
        log.info("'{}' activation is complete! Exact url: {}", environment.getProperty("spring.application.name").toUpperCase(), String.format(SWAGGER_UI_URL, host, port));
    }
}