package com.example.store.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpMessage implements Message {
    private int statusCode;
    private String text;

}
