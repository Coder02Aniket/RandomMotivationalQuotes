package com.aniket.randommotivationalapi.Service;

import com.aniket.randommotivationalapi.model.Quote;

import java.util.List;

public interface IQuoteProvider {
    Quote getQuote() throws InterruptedException;
    void fetchQuote();
    List<Quote> getCache();
}
