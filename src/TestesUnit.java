import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TestesUnit {

    @Test
    void testQuickJSONFormatterSpacesAndTabsIgnored() {
        String input = "{  \"a\" :   1 , \"b\":   [2,3] }";
        String output = JSONUtils.quickJSONFormater(input);
        assertTrue(output.contains("\"a\": 1"));
        assertTrue(output.contains("\"b\":"));
    }


    @Test
    void testQuickJSONFormatterNested() {
        String input = "{\"x\":{\"y\":{\"z\":123}}}";
        String output = JSONUtils.quickJSONFormater(input);
        assertTrue(output.contains("z\": 123"));
        assertTrue(output.lines().count() >= 4);
    }

    @Test
    void testGetJsonStringMultipleFieldsSameNameReturnsFirst() {
        String json = "{ \"text\": \"A\", \"text\": \"B\" }";
        assertEquals("A", JSONUtils.getJsonString(json, "text"));
    }

    @Test
    void testGetJsonStringValueWithEscapedBackslash() {
        String json = "{ \"path\": \"C:\\\\temp\" }";
        assertEquals("C:\\\\temp", JSONUtils.getJsonString(json, "path"));
    }

    @Test
    void testGetJsonStringMissingColon() {
        String json = "{ \"key\" \"value\" }";
        assertNull(JSONUtils.getJsonString(json, "key"));
    }

    @Test
    void testDiaAdicionarEOrdenar() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("C", "23:59"));
        d.adicionarTarefa(new Tarefa("A", "00:00"));
        d.adicionarTarefa(new Tarefa("B", "12:00"));

        List<Tarefa> t = d.getTarefas();
        assertEquals("00:00", t.get(0).getHora());
        assertEquals("12:00", t.get(1).getHora());
        assertEquals("23:59", t.get(2).getHora());
    }

    @Test
    void testDiaGetTarefasIsDefensiveCopy() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("A", "10:00"));

        List<Tarefa> copy = d.getTarefas();
        copy.clear();

        assertEquals(1, d.getTarefas().size());
    }

    @Test
    void testDataCriaDiasPorMesDistintos() {
        Data data = new Data();
        Dia jan = data.getDia(1, 1);
        Dia fev = data.getDia(2, 1);
        assertNotSame(jan, fev);
    }

    @Test
    void testDataToStringOrdenado() {
        Data data = new Data();
        data.getDia(5, 1);
        data.getDia(1, 1);
        String txt = data.toString();
        assertTrue(txt.indexOf("Mês 1") < txt.indexOf("Mês 5"));
    }

    @Test
    void testDataCriarDiaVariasVezesMesmoMesRetornaMesmoObjeto() {
        Data data = new Data();
        Dia d1 = data.getDia(3, 1);
        Dia d2 = data.getDia(3, 15);
        assertSame(d1, d2);
    }

    @Test
    void testListaTarefasAdicionarLimitesHoras() {
        ListaTarefas lista = new ListaTarefas();

        lista.adicionarTarefa("Inicio", "00:00", 1, 1);
        lista.adicionarTarefa("Fim", "23:59", 1, 2);

        assertEquals(2, lista.getData().getDia(1, 1).getTarefas().size());
        assertEquals(2, lista.getData().getDia(1, 2).getTarefas().size());
    }

    @Test
    void testListaTarefasMesMinMax() {
        ListaTarefas lista = new ListaTarefas();
        lista.adicionarTarefa("Ok", "10:00", 12, 31);
        assertEquals(1, lista.getData().getDia(12, 31).getTarefas().size());
    }

    @Test
    void testListaTarefasMesInvalido() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("X", "10:00", 0, 1));
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("X", "10:00", 13, 1));
    }

    @Test
    void testListaTarefasDiaNegativoOuZero() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("X", "10:00", 5, 0));
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("X", "10:00", 5, -1));
    }

    @Test
    void testListaTarefasFormatoHoraInvalido() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("X", "abc", 5, 1));
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("X", "1:00", 5, 1));
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("X", "10:1", 5, 1));
    }

    @Test
    void testListaTarefasMinutosInvalidos() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("X", "10:60", 3, 10));
    }

    @Test
    void testListaTarefasTituloComEspacos() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("   ", "10:00", 1, 1));
    }

    @Test
    void testTarefaCriacaoBasica() {
        Tarefa t = new Tarefa("Fazer algo", "13:45");
        assertEquals("Fazer algo", t.getTitulo());
        assertEquals("13:45", t.getHora());
    }

    @Test
    void testTarefaToStringCompleto() {
        Tarefa t = new Tarefa("Almoçar", "12:30");
        assertEquals("12:30 - Almoçar", t.toString());
    }
}
