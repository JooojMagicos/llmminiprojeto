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

/**
 * Classe responsável pela interação com um modelo de linguagem (LLM)
 * através de pedidos HTTP.
 * <p>
 * Permite enviar prompts para um endpoint remoto e obter a resposta
 * no formato JSON devolvido pela API.
 * </p>
 */
public class LLMInteractionEngine {

    /** URL do endpoint da API */
    private final String url;

    /** Chave de autenticação da API */
    private final String apiKey;

    /** Nome do modelo de linguagem a utilizar */
    private final String model;

    /** Indica se deve ser usado um mecanismo que ignora validação SSL */
    private final boolean useHack;

    /**
     * Construtor completo da classe.
     *
     * @param url URL do endpoint da API
     * @param apiKey chave de autenticação
     * @param model modelo de linguagem a utilizar
     * @param useHack indica se a validação SSL deve ser ignorada
     */
    public LLMInteractionEngine(String url, String apiKey, String model, boolean useHack) {
        this.url = url;
        this.apiKey = apiKey;
        this.model = model;
        this.useHack = useHack;
    }

    /**
     * Construtor da classe sem ignorar SSL.
     *
     * @param url URL do endpoint da API
     * @param apiKey chave de autenticação
     * @param model modelo de linguagem a utilizar
     */
    public LLMInteractionEngine(String url, String apiKey, String model) {
        this(url, apiKey, model, false);
    }

    /**
     * Escapa caracteres especiais que podem quebrar a estrutura JSON.
     *
     * @param s string original
     * @return string segura para inclusão em JSON
     */
    private static String escapeJSONString(String s) {
        if (s == null) return "";
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "")
                .replace("\t", "\\t");
    }

    /**
     * Constrói o corpo JSON do pedido no formato esperado pela API.
     *
     * @param model nome do modelo de linguagem
     * @param prompt texto a enviar para o modelo
     * @return string JSON formatada
     */
    private String buildJSON(String model, String prompt) {
        return "{"
                + "\"model\": \"" + model + "\","
                + "\"prompt\": \"" + escapeJSONString(prompt) + "\""
                + "}";
    }

    /**
     * Envia um prompt para o modelo de linguagem.
     * <p>
     * Dependendo da configuração, pode utilizar um cliente HTTP
     * normal ou um que ignora a validação SSL.
     * </p>
     *
     * @param prompt texto a enviar para o modelo
     * @return resposta da API em formato JSON
     * @throws Exception caso ocorra erro na comunicação
     */
    public String sendPrompt(String prompt) throws Exception {
        if (useHack) {
            return sendPromptIgnoreSSL(prompt);
        }
        HttpClient client = HttpClient.newHttpClient();
        return send(client, prompt);
    }

    /**
     * Envia um prompt ignorando a validação de certificados SSL.
     *
     * @param prompt texto a enviar para o modelo
     * @return resposta da API em formato JSON
     * @throws IOException erro de comunicação
     * @throws InterruptedException interrupção do pedido
     * @throws NoSuchAlgorithmException erro no algoritmo de segurança
     * @throws KeyManagementException erro na gestão de chaves SSL
     */
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

    /**
     * Realiza o envio efetivo do pedido HTTP para a API.
     *
     * @param client cliente HTTP a utilizar
     * @param prompt texto a enviar para o modelo
     * @return resposta da API em formato JSON
     * @throws IOException erro de comunicação
     * @throws InterruptedException interrupção do pedido
     */
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
