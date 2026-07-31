package com.aniket.randommotivationalapi;

import com.aniket.randommotivationalapi.Service.IQuoteProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class RandomMotivationalApiApplicationTests {

    @Autowired
    private IQuoteProvider quoteProvider;

    @Test
    void contextLoads() {



    }

    @Test
    void provideQuote() throws InterruptedException {
        if(quoteProvider.getQuote()!=null){
            System.out.println("Quote is provided");
        }
        else{
            System.out.println("Quote is not provided");
        }

        Assert.notNull(quoteProvider.getQuote(),"Quote is null");
    }

    //write a sample test if cache has quote at startup

    @Test()
    void quotePresentOnStartup() throws InterruptedException {
        if(quoteProvider.getCache()!=null){
            System.out.println(quoteProvider.getCache());
            System.out.println("Quote is present on startup");
        }
        else{
            System.out.println("Quote is not present on startup");
        }

        Assert.notNull(quoteProvider.getQuote(),"Quote is null on startup");
    }

}
