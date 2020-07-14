package com.pyruby.cloudfunc;

import com.google.cloud.functions.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LoggerFunctionTest {

    @Test
    public void service_respondsHelloWorld_andLogsMessage() throws Exception {
        StringWriter out = new StringWriter();
        BufferedWriter writer = new BufferedWriter(out);
        HttpResponse res = mock(HttpResponse.class);
        when(res.getWriter()).thenReturn(writer);
        Logger logger = mock(Logger.class);
        ArgumentCaptor<String> logsCaptor = ArgumentCaptor.forClass(String.class);

        new LoggerFunction(logger).service(null, res);

        writer.flush();
        assertThat(out.toString()).isEqualTo("Hello World!");

        verify(logger).info(logsCaptor.capture());
        assertThat(logsCaptor.getAllValues()).containsExactly("I am an info log!");
    }
}