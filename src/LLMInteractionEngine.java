
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;


public class LLMInteractionEngine {

    private final String url;
    private final String apiKey;
    private final String model;
    private final boolean useHack;

    public LLMInteractionEngine(String url, String apiKey, String model, boolean useHack) {
        this.url = url;
        this.apiKey = apiKey;
        this.model = model;
        this.useHack = useHack;
    }


    public String getUrl(){
        return this.url;
    }

    public String getApiKey(){
        return this.apiKey;
    }

    public String getModel(){
        return this.model;
    }

    public boolean getUseHack(){
        return this.useHack;
    }
    public LLMInteractionEngine(String url, String apiKey, String model) {
        this(url, apiKey, model, false);
    }

    // ---------- ESCAPA CARACTERES QUEBRAM JSON ----------
    private static String escapeJSONString(String s) {
        if (s == null) return "";
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "")
                .replace("\t", "\\t");
    }


    // ---------- CONSTROI JSON NO FORMATO CHAT-BASED ----------
    private String buildJSON(String model, String prompt) {
        return "{"
                + "\"model\": \"" + model + "\","
                + "\"prompt\": \"" + escapeJSONString(prompt) + "\""
                + "}";
    }


    // ---------- ENVIA PROMPT ----------
    public String sendPrompt(String prompt) throws Exception {
        if (useHack) {
            return sendPromptIgnoreSSL(prompt);
        }
        HttpClient client = HttpClient.newHttpClient();
        return send(client, prompt);
    }

    // ---------- ENVIA IGNORANDO SSL ----------
    private String sendPromptIgnoreSSL(String prompt)
            throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{ new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] c, String a) {}
            public void checkServerTrusted(X509Certificate[] c, String a) {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        }}, new SecureRandom());

        HttpClient insecure = HttpClient.newBuilder().sslContext(sc).build();
        return send(insecure, prompt);
    }

    // ---------- ENVIO REAL ----------
    private String send(HttpClient client, String prompt) throws IOException, InterruptedException {
        String json = buildJSON(model, prompt);



        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());



        return resp.body() != null ? resp.body() : "";
    }
}
