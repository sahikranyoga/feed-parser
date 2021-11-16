package com.sahikran.httputil;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLContext;

import org.junit.jupiter.api.Test;

public class RestHandlerTest {

    @Test
    public void whenUrlPassed_returnHttpClient() 
        throws IOException, URISyntaxException, NoSuchAlgorithmException, InterruptedException, ExecutionException, KeyManagementException{
        HttpRequest httpRequest = HttpRequest.newBuilder()
                                    .uri(new URI("https://shodhganga.inflibnet.ac.in/simple-search?query=yoga&filter_field_1=subject&filter_type_1=equals&filter_value_1=Yoga&sort_by=score&order=desc&rpp=300&etal=0&start=0"))
                                    .GET()
                                    .build();

        SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
        sslContext.init(null, null, null);

        HttpClient httpClient = HttpClient.newBuilder()
                                .sslContext(sslContext)
                                .followRedirects(Redirect.NORMAL)
                                .connectTimeout(Duration.ofSeconds(20))
                                .build();

        CompletableFuture<HttpResponse<InputStream>> responseStream = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        
        assertNotNull(responseStream);
    }
}
