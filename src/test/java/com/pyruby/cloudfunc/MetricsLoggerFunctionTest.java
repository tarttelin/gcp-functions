package com.pyruby.cloudfunc;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.verification.VerificationModeFactory;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MetricsLoggerFunctionTest {

    @Test
    public void service_respondsHelloWorld_andLogsMessage() throws Exception {
        StringWriter out = new StringWriter();
        BufferedWriter writer = new BufferedWriter(out);
        HttpResponse res = mock(HttpResponse.class);
        when(res.getWriter()).thenReturn(writer);
        HttpRequest req = mock(HttpRequest.class);
        when(req.getFirstQueryParameter("total")).thenReturn(Optional.empty());
        when(req.getFirstQueryParameter("success")).thenReturn(Optional.empty());
        when(req.getFirstQueryParameter("error")).thenReturn(Optional.empty());
        Logger logger = mock(Logger.class);
        ArgumentCaptor<String> logsCaptor = ArgumentCaptor.forClass(String.class);

        new MetricsLoggerFunction(logger).service(req, res);

        writer.flush();
        assertThat(out.toString()).isEqualTo("Hello World!");

        verify(logger, VerificationModeFactory.atLeastOnce()).info(logsCaptor.capture());
        assertThat(logsCaptor.getAllValues()).contains("I am an info log!");
    }

    @Test
    public void service_logsMetricInfo_fromQueryParams() throws Exception {
        StringWriter out = new StringWriter();
        BufferedWriter writer = new BufferedWriter(out);
        HttpResponse res = mock(HttpResponse.class);
        when(res.getWriter()).thenReturn(writer);
        HttpRequest req = mock(HttpRequest.class);
        when(req.getFirstQueryParameter("total")).thenReturn(Optional.of("2"));
        when(req.getFirstQueryParameter("success")).thenReturn(Optional.of("1"));
        when(req.getFirstQueryParameter("error")).thenReturn(Optional.of("1"));
        Logger logger = mock(Logger.class);

        new MetricsLoggerFunction(logger).service(req, res);
        writer.flush();

        ArgumentCaptor<String> logsCaptor = ArgumentCaptor.forClass(String.class);
        verify(logger, VerificationModeFactory.atLeastOnce()).info(logsCaptor.capture());
        assertThat(logsCaptor.getAllValues()).contains("TRANSFORMATION_METRIC total=2 success=1 error=1");
        assertThat(logsCaptor.getAllValues()).contains("I am an info log!");
    }

    @Test
    public void service_logsMetricInfo_withDefaulValues_givenNoQueryParams() throws Exception {
        StringWriter out = new StringWriter();
        BufferedWriter writer = new BufferedWriter(out);
        HttpResponse res = mock(HttpResponse.class);
        when(res.getWriter()).thenReturn(writer);
        HttpRequest req = mock(HttpRequest.class);
        when(req.getFirstQueryParameter("total")).thenReturn(Optional.empty());
        when(req.getFirstQueryParameter("success")).thenReturn(Optional.empty());
        when(req.getFirstQueryParameter("error")).thenReturn(Optional.empty());
        Logger logger = mock(Logger.class);

        new MetricsLoggerFunction(logger).service(req, res);
        writer.flush();

        ArgumentCaptor<String> logsCaptor = ArgumentCaptor.forClass(String.class);
        verify(logger, VerificationModeFactory.atLeastOnce()).info(logsCaptor.capture());
        assertThat(logsCaptor.getAllValues()).contains("TRANSFORMATION_METRIC total=0 success=0 error=0");
        assertThat(logsCaptor.getAllValues()).contains("I am an info log!");
    }
}