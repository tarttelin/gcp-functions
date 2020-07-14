package com.pyruby.cloudfunc;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.util.logging.Logger;

public class LoggerFunction implements HttpFunction {

    private final Logger logger;

    public LoggerFunction() {
        this(Logger.getLogger(LoggerFunction.class.getName()));
    }

    public LoggerFunction(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        logger.info("I am an info log!");
        BufferedWriter writer = response.getWriter();
        writer.write("Hello World!");
    }
}
