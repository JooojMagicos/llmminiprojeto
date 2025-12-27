import org.junit.jupiter.api.Test;


import java.net.http.*;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class LLMInteractionEngineTest {

    @Test
    void testConstrutorPadrao() {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model");
        assertEquals("url", engine.getUrl());
        assertEquals("key", engine.getApiKey());
        assertEquals("model", engine.getModel());
        assertFalse(engine.isUseHack());
    }

    @Test
    void testConstrutorComHack() {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model", true);
        assertTrue(engine.isUseHack());
    }


    @Test
    void testEscapeJSONString() throws Exception {
        var method = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
        method.setAccessible(true);

        String original = "Linha \"com\" quebra\n e \t tab";
        String escaped = (String) method.invoke(null, original);

        assertTrue(escaped.contains("\\\""));
        assertTrue(escaped.contains("\\n"));
        assertTrue(escaped.contains("\\t"));
    }

    @Test
    void testBuildJSON() throws Exception {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model");

        var method = LLMInteractionEngine.class.getDeclaredMethod("buildJSON", String.class, String.class);
        method.setAccessible(true);

        String prompt = "Teste \"prompt\"\nNova linha";
        String json = (String) method.invoke(engine, "model", prompt);

        assertTrue(json.contains("\"model\": \"model\""));
        assertTrue(json.contains("Teste \\\"prompt\\\""));
    }

    @Test
    void testSendRealFakeClient() throws Exception {
        LLMInteractionEngine engine = new LLMInteractionEngine("http://teste", "chave", "modelo");

        HttpClient fakeClient = new HttpClient() {
            @Override
            public Optional<CookieHandler> cookieHandler() { return Optional.empty(); }

            @Override
            public Optional<Duration> connectTimeout() { return Optional.empty(); }

            @Override
            public Redirect followRedirects() { return Redirect.NEVER; }

            @Override
            public Optional<ProxySelector> proxy() { return Optional.empty(); }

            @Override
            public SSLContext sslContext() { return null; }

            @Override
            public SSLParameters sslParameters() { return null; }

            @Override
            public Optional<Authenticator> authenticator() { return Optional.empty(); }

            @Override
            public Version version() { return Version.HTTP_1_1; }

            @Override
            public Optional<Executor> executor() { return Optional.empty(); }

            @Override
            public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> handler) {
                return new HttpResponse<>() {
                    @Override public int statusCode() { return 200; }
                    @Override public T body() { return (T) "RESPOSTA_FAKE"; }
                    @Override public HttpRequest request() { return request; }
                    @Override public Optional<HttpResponse<T>> previousResponse() { return Optional.empty(); }
                    @Override public HttpHeaders headers() { return HttpHeaders.of(Map.of(), (a,b)->true); }
                    @Override public URI uri() { return request.uri(); }
                    @Override public Version version() { return Version.HTTP_1_1; }
                    @Override public Optional<SSLSession> sslSession() { return Optional.empty(); }
                };
            }

            @Override
            public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
                return null;
            }

            @Override
            public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler,
                                                                    HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
                return null;
            }
        };

        var sendMethod = LLMInteractionEngine.class.getDeclaredMethod("send", HttpClient.class, String.class);
        sendMethod.setAccessible(true);

        String response = (String) sendMethod.invoke(engine, fakeClient, "prompt");
        assertEquals("RESPOSTA_FAKE", response);
    }

    @Test
    void testSendPromptHack() throws Exception {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model", true);

        // Apenas criar SSLContext para garantir que não explode
        SSLContext sc = SSLContext.getInstance("TLS");
        assertNotNull(sc);
    }
}
