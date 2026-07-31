package com.aniket.randommotivationalapi.model;


import lombok.Data;

import java.io.Serializable;

@Data

public class Quote implements Serializable {
    private String quote;
    private String author ;
}
