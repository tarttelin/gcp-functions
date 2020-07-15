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
        Long total = request.getFirstQueryParameter("total").map(Long::valueOf).orElse(0L);
        Long success = request.getFirstQueryParameter("success").map(Long::valueOf).orElse(0L);
        Long error = request.getFirstQueryParameter("error").map(Long::valueOf).orElse(0L);
        String metricLog = String.format("TRANSFORMATION_METRIC total=%s success=%s error=%s", total, success, error);
        logger.info(metricLog);
        logger.info("I am an info log!");
        BufferedWriter writer = response.getWriter();
        writer.write("Hello World!");
    }
}
