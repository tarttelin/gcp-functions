package com.pyruby.cloudfunc;

import com.google.cloud.functions.HttpResponse;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HelloWorldTest {

    @Test
    public void service_writesHelloWorld_toResponseWriter() throws Exception {
        StringWriter out = new StringWriter();
        BufferedWriter writer = new BufferedWriter(out);
        HttpResponse res = mock(HttpResponse.class);
        when(res.getWriter()).thenReturn(writer);

        new HelloWorld().service(null, res);

        writer.flush();
        assertThat(out.toString()).isEqualTo("Hello world");
    }
}