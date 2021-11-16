package com.sahikran.httputil;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import javax.net.ssl.SSLContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sahikran.exception.RestHandlerException;

@Service
@Qualifier("httpsRestHandler")
public class HttpsRestHandler implements RestHandler {

    @Override
    public CompletableFuture<HttpClient> getHttpClient(boolean isSSLEnabled, Duration timeout) {
        return CompletableFuture.supplyAsync(() -> {
                SSLContext sslContext = null;

                if(isSSLEnabled){
                    try {
                        sslContext = SSLContext.getInstance("TLSv1.3");
                        sslContext.init(null, null, null);
                    } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        throw new RestHandlerException("unable to initialize ssl context ", e);
                    }
                }

                return HttpClient.newBuilder()
                                .sslContext(sslContext)
                                .followRedirects(Redirect.NORMAL)
                                .connectTimeout(timeout)
                                .build();
            }
        );
    }

    @Override
    public Function<HttpClient, CompletableFuture<HttpResponse<InputStream>>> invokeARestCall(String pageUrl) {
        return httpClient -> {
            HttpRequest httpRequest;
            try {
                httpRequest = HttpRequest.newBuilder()
                                            .uri(new URI(pageUrl))
                                            .header("Accept-Encoding", "gzip")
                                            .GET()
                                            .build();
            } catch (URISyntaxException e) {
                throw new RestHandlerException("httprequest cant be initiliazed ", e);
            }
            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        };
    }
    
}
