package com.aniket.randommotivationalapi.Controller;

import com.aniket.randommotivationalapi.Service.QuoteProvider;
import com.aniket.randommotivationalapi.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller
@RequestMapping("/quote")
public class QuoteController {


    @Autowired
    QuoteProvider quoteProvider ;


    @GetMapping(value={"/",""})
    public String getRandomQuoteFromArray(Model model) throws InterruptedException {
        Quote quote = quoteProvider.getQuote();

        model.addAllAttributes(
                Map.of("quote", quote.getQuote(),
                        "author", quote.getAuthor())
        );



        return "index";
    }

}
