package com.aniket.randommotivationalapi;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;


@SpringBootApplication
@EnableScheduling
public class RandomMotivationalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RandomMotivationalApiApplication.class, args);
    }


    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate( );
    }

    @Bean("reactive")
    public WebClient webClient() throws SSLException {

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE) // FOR TESTING ONLY
                // For production, use .trustManager(new File("path/to/cert.crt"))
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(sslSpec -> sslSpec.sslContext(sslContext));

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return webClient;

    }



}
