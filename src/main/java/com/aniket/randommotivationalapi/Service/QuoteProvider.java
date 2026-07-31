package com.aniket.randommotivationalapi.Service;


import com.aniket.randommotivationalapi.model.Quote;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service

public class QuoteProvider implements IQuoteProvider {


    @Getter
    private  List<Quote> cache = null;

    @Autowired
    @Qualifier("reactive")
    private WebClient webClient;

    @Autowired
    private ObjectMapper objMapper;

    @Override
    public  Quote getQuote() throws InterruptedException {

        synchronized (cache){
            System.out.println(Thread.currentThread().getName());

            int count = 0 ;
            while(cache.isEmpty()){
                cache.wait();
                System.out.println("this block is executed for %d times".formatted(++count));
            }
            return cache.removeFirst();
        }


    }

    @Scheduled(fixedRate = 10000 )
    public void fetchQuote(){
        if(cache.size()>=100){
            return ;
        }
        System.out.println("Fetching Quote");
        webClient.get()
                .uri("https://motivational-spark-api.vercel.app/api/quotes/random/1")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(List.class)
                .subscribe(this::onResponse);


    }

    private void onResponse(List<Map<String,Object>> response){
        System.out.println(response);
        System.out.println(response.getClass().getTypeName());
        System.out.println(response.get(0).getClass().getTypeName());
        System.out.println(response.get(0).getClass().getTypeName());

        Quote quote = new Quote();
        quote.setQuote(response.get(0).get("quote").toString());
        quote.setAuthor(response.get(0).get("author").toString());
        synchronized (cache){
            cache.add(quote);
            cache.notifyAll();

        }
    }

    @PreDestroy
    private void saveBeforeShutdown() {
        //Create a file for saving current state of the system before shutting down...
/**
 * Changing Unreadable serialization to readable JSON
 */
//        ObjectOutputStream oos = new ObjectOutputStream(
//                new java.io.FileOutputStream("cache.ser")
//        );
//        oos.writeObject(cache);


//        oos.close();

        objMapper.writerWithDefaultPrettyPrinter().writeValue(new File("cache.json"), cache);


    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void loadCacheFromFile() throws IOException, ClassNotFoundException {
        //Load the cache from the file if it exists
        java.io.File file = new java.io.File("cache.ser");
        if(file.exists()){
            ObjectInputStream ois = new ObjectInputStream(
                    new java.io.FileInputStream(file)
            );
            cache = (List<Quote>) ois.readObject();

            //Renaming the file once loaded as we are shifting to json payload structure
            ois.close();
            file.renameTo(new java.io.File("cache_old.ser"));
        }else if (new File("cache.json").exists()){
            cache = objMapper.readValue(new java.io.File("cache.json"), new TypeReference<List<Quote>>(){});
        }else {
            cache = new LinkedList<>();
        }

    }

}
