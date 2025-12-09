import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JSONUtilsTest {

    @Test
    void testQuickJSONFormatter() {
        String input = "{\"a\":1,\"b\":2}";
        String formatted = JSONUtils.quickJSONFormater(input);

        assertTrue(formatted.contains("\n"));
        assertTrue(formatted.contains("a"));
    }

    @Test
    void testGetJsonStringNormal() {
        String json = "{\"text\":\"OK\"}";
        assertEquals("OK", JSONUtils.getJsonString(json, "text"));
    }

    @Test
    void testGetJsonStringComEscape() {
        String json = "{\"text\":\"Linha\\nNova\"}";
        assertEquals("Linha\\nNova", JSONUtils.getJsonString(json, "text"));
    }

    @Test
    void testGetJsonStringInexistente() {
        String json = "{\"a\":\"b\"}";
        assertNull(JSONUtils.getJsonString(json, "x"));
    }


    @Test
    void quickJSONFormaterComObjetoSimples() {
        String input = "{\"a\":1,\"b\":[{\"x\":10},{\"y\":20}],\"z\":\"ok\"}";
        String formatted = JSONUtils.quickJSONFormater(input);
        // espera que contenha quebras de linha formatadas e chaves/colchetes
        assertTrue(formatted.contains("\n"));
        assertTrue(formatted.contains("\"z\": \"ok\"") || formatted.contains("\"z\":\"ok\""));
    }

    @Test
    void getJsonStringCasoNormal() {
        String json = "{\"text\":\"OK\"}";
        assertEquals("OK", JSONUtils.getJsonString(json, "text"));
    }

    @Test
    void getJsonStringComEscapesENovasLinhas() {
        String json = "{\"text\":\"Linha\\nCom \\\"aspas\\\" e \\\\ barras\"}";
        String extracted = JSONUtils.getJsonString(json, "text");
        assertEquals("Linha\\nCom \\\"aspas\\\" e \\\\ barras", extracted);
    }

    @Test
    void getJsonStringChaveAusenteRetornaNull() {
        String json = "{\"a\":\"b\"}";
        assertNull(JSONUtils.getJsonString(json, "missing"));
    }

    @Test
    void getJsonStringComDoisCamposMesmoNomeRetornaPrimeiro() {
        String json = "{\"text\":\"primeiro\",\"text\":\"segundo\"}";
        // implementação é ingênua: deve retornar o primeiro valor encontrado
        assertEquals("primeiro", JSONUtils.getJsonString(json, "text"));
    }

    @Test
    void quickJSONFormaterIgnoraStringsInternasCorretamente() {
        String tricky = "{\"a\":\"{notjson}\",\"b\":1}";
        String out = JSONUtils.quickJSONFormater(tricky);
        assertTrue(out.contains("{notjson}"));
    }
}
