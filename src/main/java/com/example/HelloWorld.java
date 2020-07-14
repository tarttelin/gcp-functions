package com.example;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.util.logging.Logger;

public class HelloWorld implements HttpFunction {

    private static final Logger logger = Logger.getLogger(HelloWorld.class.getName());

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        logger.info("I am an info log!");
        BufferedWriter writer = response.getWriter();
        writer.write("Hello World!");
    }
}
