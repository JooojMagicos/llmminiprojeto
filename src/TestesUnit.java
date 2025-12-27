import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.net.http.HttpClient;
import java.util.Map;

class TudoTestFinal {


    ListaTarefas lista = new ListaTarefas();


    @Test
    void testQuickJSONFormatterBasic() {
        String json = "{ \"a\":1, \"b\":2 }";
        String out = JSONUtils.quickJSONFormater(json);
        assertTrue(out.contains("\"a\": 1"));
        assertTrue(out.contains("\"b\": 2"));
    }

    @Test
    void testQuickJSONFormatterComplex() {
        String json = "{\"x\":{\"y\":[1,2,{\"z\":3}]}}";
        String out = JSONUtils.quickJSONFormater(json);
        assertTrue(out.contains("z\": 3"));
        assertTrue(out.lines().count() > 4);
    }

    @Test
    void testGetJsonStringNormal() {
        String json = "{ \"msg\": \"hello\" }";
        assertEquals("hello", JSONUtils.getJsonString(json, "msg"));
    }

    @Test
    void testGetJsonStringNotFound() {
        String json = "{ \"abc\": \"def\" }";
        assertNull(JSONUtils.getJsonString(json, "x"));
    }

    @Test
    void testGetJsonStringMultipleValues() {
        String json = "{ \"k\": \"first\", \"k\": \"second\" }";
        assertEquals("first", JSONUtils.getJsonString(json, "k"));
    }

    @Test
    void testGetJsonStringEscapedQuotes() {
        String json = "{ \"t\": \"he said \\\"hi\\\"\" }";
        assertEquals("he said \\\"hi\\\"", JSONUtils.getJsonString(json, "t"));
    }

    @Test
    void testGetJsonStringMissingColon() {
        String json = "{ \"t\" \"x\" }";
        assertNull(JSONUtils.getJsonString(json, "t"));
    }

    @Test
    void testTarefaCampos() {
        Tarefa t = new Tarefa("Study", "10:30");
        assertEquals("Study", t.getTitulo());
        assertEquals("10:30", t.getHora());
    }

    @Test
    void testTarefaToString() {
        Tarefa t = new Tarefa("Cook", "18:00");
        assertEquals("18:00 - Cook", t.toString());
    }

    @Test
    void testDiaAdicionarEOrdenar() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("B", "23:00"));
        d.adicionarTarefa(new Tarefa("A", "00:01"));
        d.adicionarTarefa(new Tarefa("C", "15:10"));
        List<Tarefa> tarefas = d.getTarefas();
        assertEquals("00:01", tarefas.get(0).getHora());
        assertEquals("15:10", tarefas.get(1).getHora());
        assertEquals("23:00", tarefas.get(2).getHora());
    }

    @Test
    void testDiaGetTarefasIsDefensive() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("A", "09:00"));
        List<Tarefa> copy = d.getTarefas();
        copy.clear();
        assertEquals(1, d.getTarefas().size());
    }

    @Test
    void testDiaToString() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("X", "11:22"));
        String s = d.toString();
        assertTrue(s.contains("11:22 - X"));
    }

    @Test
    void testDataGetDiaCriaNovo() {
        Data data = new Data();
        Dia d = data.getDia(1, 1);
        assertNotNull(d);
    }

    @Test
    void testDataGetDiaMesmoMes() {
        Data data = new Data();
        Dia d1 = data.getDia(1, 5);
        Dia d2 = data.getDia(1, 10);
        assertSame(d1, d2);
    }


    @Test
    void testGetMeses() {
        Data data = new Data();

        Dia d1 = data.getDia(1, 10);
        Dia d3 = data.getDia(3, 5);
        Dia d7 = data.getDia(7, 20);

        Map<Integer, Dia> meses = data.getMeses();

        assertNotNull(meses);

        assertEquals(3, meses.size());

        assertTrue(meses.containsKey(1));
        assertTrue(meses.containsKey(3));
        assertTrue(meses.containsKey(7));

        assertSame(d1, meses.get(1));
        assertSame(d3, meses.get(3));
        assertSame(d7, meses.get(7));

        assertSame(meses, data.getMeses());
    }


    @Test
    void testDataToString() {
        Data data = new Data();
        data.getDia(2, 1).adicionarTarefa(new Tarefa("Test", "10:00"));
        String s = data.toString();
        assertTrue(s.contains("Mês 2"));
        assertTrue(s.contains("10:00 - Test"));
    }

    @Test
    void testListaTarefasAdicionarNormal() {
        ListaTarefas lt = new ListaTarefas();
        lt.adicionarTarefa("A", "12:00", 1, 1);
        assertEquals(1, lt.getData().getDia(1, 1).getTarefas().size());
    }

    @Test
    void testListaTarefasHoraLimites() {
        ListaTarefas lt = new ListaTarefas();
        lt.adicionarTarefa("Min", "00:00", 5, 2);
        lt.adicionarTarefa("Max", "23:59", 5, 3);
        assertEquals(2, lt.getData().getDia(5, 2).getTarefas().size());
        assertEquals(2, lt.getData().getDia(5, 3).getTarefas().size());
    }

    @Test
    void testListaTarefasMesInvalido() {
        ListaTarefas lt = new ListaTarefas();
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("X", "10:00", 0, 1));
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("X", "10:00", 13, 1));
    }

    @Test
    void testListaTarefasDiaInvalido() {
        ListaTarefas lt = new ListaTarefas();
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("X", "10:00", 5, 0));
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("X", "10:00", 5, -5));
    }

    @Test
    void testListaTarefasHoraFormatoInvalido() {
        ListaTarefas lt = new ListaTarefas();
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("X", "99:10", 3, 3));
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("X", "10:99", 3, 3));
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("X", "aaa", 3, 3));
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("X", "1:1", 3, 3));
    }

    @Test
    void testListaTarefasTituloInvalido() {
        ListaTarefas lt = new ListaTarefas();
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("", "10:00", 1, 1));
        assertThrows(IllegalArgumentException.class, () -> lt.adicionarTarefa("   ", "10:00", 1, 1));
    }

    @Test
    void testListaTarefasGetData() {
        ListaTarefas lt = new ListaTarefas();
        assertNotNull(lt.getData());
    }





        @Test
        void testEscapeJsonString_Null() throws Exception {
            var method = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
            method.setAccessible(true);

            String r = (String) method.invoke(null, (String) null);
            assertEquals("", r);
        }

        @Test
        void testEscapeJsonString_Escapes() throws Exception {
            var method = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
            method.setAccessible(true);

            String r = (String) method.invoke(null, "a\"b\\c\nd\te");
            assertEquals("a\\\"b\\\\c\\nd\\te", r);
        }

        @Test
        void testBuildJSON() throws Exception {
            LLMInteractionEngine eng = new LLMInteractionEngine("url", "key", "model");

            var method = LLMInteractionEngine.class.getDeclaredMethod("buildJSON", String.class, String.class);
            method.setAccessible(true);

            String json = (String) method.invoke(eng, "mymodel", "hello \"world\"");
            assertEquals("{\"model\": \"mymodel\",\"prompt\": \"hello \\\"world\\\"\"}", json);
        }



        @Test
        void testConstructors() {
            LLMInteractionEngine e1 = new LLMInteractionEngine("U", "K", "M");
            LLMInteractionEngine e2 = new LLMInteractionEngine("U", "K", "M", true);

            assertNotNull(e1);
            assertNotNull(e2);
        }



    @Test
    void testEscapeJSONString_null() throws Exception {
        Method m = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
        m.setAccessible(true);
        String r = (String) m.invoke(null, (String) null);
        assertEquals("", r);
    }

    @Test
    void testEscapeJSONString_specialChars() throws Exception {
        Method m = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
        m.setAccessible(true);
        String input = "a\"b\\c\nd\te\rf";
        String expected = "a\\\"b\\\\c\\nd\\tef";
        String r = (String) m.invoke(null, input);
        assertEquals(expected, r);
    }

    @Test
    void testBuildJSON2() throws Exception {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model");
        Method m = LLMInteractionEngine.class.getDeclaredMethod("buildJSON", String.class, String.class);
        m.setAccessible(true);
        String json = (String) m.invoke(engine, "mymodel", "hello \"world\"");
        assertEquals("{\"model\": \"mymodel\",\"prompt\": \"hello \\\"world\\\"\"}", json);
    }

    @Test
    void testConstructors2() {
        LLMInteractionEngine e1 = new LLMInteractionEngine("U", "K", "M");
        LLMInteractionEngine e2 = new LLMInteractionEngine("U", "K", "M", true);
        assertNotNull(e1);
        assertNotNull(e2);
    }

    @Test
    void testSendPrompt_useHackTrue() throws Exception {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model", true);
        // apenas verificar que o método existe e pode ser chamado com reflection
        Method m = LLMInteractionEngine.class.getDeclaredMethod("sendPromptIgnoreSSL", String.class);
        m.setAccessible(true);
        // não chamamos realmente para não ir à internet
        assertNotNull(m);
    }

    @Test
    void testSendPrompt_useHackFalse() throws Exception {
        LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model", false);
        Method m = LLMInteractionEngine.class.getDeclaredMethod("send", HttpClient.class, String.class);
        m.setAccessible(true);
        assertNotNull(m);
    }




        @Test
        void testEscapeJSONString_null2() throws Exception {
            Method m = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
            m.setAccessible(true);
            String result = (String) m.invoke(null, (String) null);
            assertEquals("", result);
        }

        @Test
        void testEscapeJSONString_specialChars2() throws Exception {
            Method m = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
            m.setAccessible(true);
            String input = "a\"b\\c\nd\te\rf";
            String expected = "a\\\"b\\\\c\\nd\\tef";
            String result = (String) m.invoke(null, input);
            assertEquals(expected, result);
        }

        @Test
        void testBuildJSON3() throws Exception {
            LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model");
            Method m = LLMInteractionEngine.class.getDeclaredMethod("buildJSON", String.class, String.class);
            m.setAccessible(true);
            String json = (String) m.invoke(engine, "mymodel", "hello \"world\"");
            assertEquals("{\"model\": \"mymodel\",\"prompt\": \"hello \\\"world\\\"\"}", json);
        }

        @Test
        void testConstructors3() {
            LLMInteractionEngine e1 = new LLMInteractionEngine("url", "key", "model");
            LLMInteractionEngine e2 = new LLMInteractionEngine("url", "key", "model", true);
            assertNotNull(e1);
            assertNotNull(e2);
        }

        @Test
        void testSendPrompt_useHackTrue_exists() throws Exception {
            LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model", true);
            Method m = LLMInteractionEngine.class.getDeclaredMethod("sendPromptIgnoreSSL", String.class);
            m.setAccessible(true);
            assertNotNull(m); // verifica que o método existe e pode ser acessado
        }

        @Test
        void testSendPrompt_useHackFalse_exists() throws Exception {
            LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model", false);
            Method m = LLMInteractionEngine.class.getDeclaredMethod("send", HttpClient.class, String.class);
            m.setAccessible(true);
            assertNotNull(m); // verifica que o método existe e pode ser acessado
        }



        @Test
        void testEscapeJSONString_null3() throws Exception {
            Method method = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(null, (String) null);
            assertEquals("", result);
        }

        @Test
        void testEscapeJSONString_specialChars3() throws Exception {
            Method method = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
            method.setAccessible(true);
            String input = "a\"b\\c\nd\te\rf";
            String expected = "a\\\"b\\\\c\\nd\\tef";
            String result = (String) method.invoke(null, input);
            assertEquals(expected, result);
        }

        @Test
        void testBuildJSON4() throws Exception {
            LLMInteractionEngine engine = new LLMInteractionEngine("url", "key", "model");
            Method method = LLMInteractionEngine.class.getDeclaredMethod("buildJSON", String.class, String.class);
            method.setAccessible(true);
            String json = (String) method.invoke(engine, "mymodel", "hello \"world\"");
            assertEquals("{\"model\": \"mymodel\",\"prompt\": \"hello \\\"world\\\"\"}", json);
        }

        @Test
        void testConstructors4() {
            LLMInteractionEngine e1 = new LLMInteractionEngine("url", "key", "model");
            LLMInteractionEngine e2 = new LLMInteractionEngine("url", "key", "model", true);
            assertNotNull(e1);
            assertNotNull(e2);
        }

        @Test
        void testSendPrompt_methodsExist() throws Exception {
            LLMInteractionEngine engineHack = new LLMInteractionEngine("url", "key", "model", true);
            LLMInteractionEngine engineNoHack = new LLMInteractionEngine("url", "key", "model", false);

            Method sendPromptIgnoreSSL = LLMInteractionEngine.class.getDeclaredMethod("sendPromptIgnoreSSL", String.class);
            sendPromptIgnoreSSL.setAccessible(true);
            assertNotNull(sendPromptIgnoreSSL);

            Method send = LLMInteractionEngine.class.getDeclaredMethod("send", HttpClient.class, String.class);
            send.setAccessible(true);
            assertNotNull(send);
        }


    static String apiKey = "sk-51lNzxys8gwcmz3uluLVGQ";
    static String url = "https://modelos.ai.ulusofona.pt/v1/completions";
    static String model = "gpt-4-turbo";


    static class FakeLLMEngine extends LLMInteractionEngine {

        private final boolean hack;

        public FakeLLMEngine(boolean useHack) {
            super("url", "key", "model", useHack);
            this.hack = useHack;
        }

        @Override
        public String sendPrompt(String prompt) {
            if (hack) {
                return "{\"text\": \"OK (useHack)\"}";
            }
            return "{\"text\": \"OK\"}";
        }
    }


    @Test
    void testSendPromptExists() throws Exception {
        Method m = LLMInteractionEngine.class.getDeclaredMethod("sendPrompt", String.class);
        assertNotNull(m);
    }


    @Test
    void testBuildJSON_privateMethod() throws Exception {
        LLMInteractionEngine engine =
                new LLMInteractionEngine(url, apiKey, model, false);

        Method buildJSON =
                LLMInteractionEngine.class.getDeclaredMethod(
                        "buildJSON", String.class, String.class);

        buildJSON.setAccessible(true);

        String result = (String) buildJSON.invoke(
                engine, "test-model", "Olá \"mundo\"");

        assertEquals(
                "{\"model\": \"test-model\",\"prompt\": \"Olá \\\"mundo\\\"\"}",
                result
        );
    }




    @Test
    void testEscapeJSONString_nullAndSpecialChars() throws Exception {
        Method escape = LLMInteractionEngine.class.getDeclaredMethod("escapeJSONString", String.class);
        escape.setAccessible(true);

        // null retorna ""
        assertEquals("", escape.invoke(null, (String) null));

        // caracteres especiais
        String input = "a\"b\\c\nd\te\rf";
        String esperado = "a\\\"b\\\\c\\nd\\tef";
        assertEquals(esperado, escape.invoke(null, input));
    }


    @Test
    void testConstructors5() {
        LLMInteractionEngine e1 = new LLMInteractionEngine(url, apiKey, model);
        LLMInteractionEngine e2 = new LLMInteractionEngine(url, apiKey, model, true);
        assertNotNull(e1);
        assertNotNull(e2);
    }



}





