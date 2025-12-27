import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ListaTarefasTest {

    @Test
    void testAdicionarTarefaValida() {
        ListaTarefas lista = new ListaTarefas();
        lista.adicionarTarefa("Teste", "10:00", 5, 3);

        var tarefas = lista.getData().getDia(5, 3).getTarefas();
        assertEquals(1, tarefas.size());
        assertEquals("10:00", tarefas.get(0).getHora());
    }

    @Test
    void testAdicionarTarefaTituloVazio() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("", "10:00", 5, 3));
    }

    @Test
    void testAdicionarTarefaHoraInvalida() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("Teste", "99:00", 5, 3));
    }

    @Test
    void testAdicionarTarefaDiaInvalido() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("Teste", "10:00", 2, 30)); // fevereiro tem 28
    }

    @Test
    void testToString() {
        ListaTarefas lista = new ListaTarefas();
        lista.adicionarTarefa("Teste", "10:00", 1, 1);

        String out = lista.toString();
        assertTrue(out.contains("10:00 - Teste"));
    }


    @Test
    void adicionarValidaEInsere() {
        ListaTarefas lista = new ListaTarefas();
        lista.adicionarTarefa("Titulo", "00:00", 1, 1);
        assertEquals(1, lista.getData().getDia(1, 1).getTarefas().size());
    }

    @Test
    void tituloNuloOuVazioDisparaErro() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa(null, "10:00", 1, 1));
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("   ", "10:00", 1, 1));
    }

    @Test
    void formatosDeHoraInvalidosDisparamErro() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "1:00", 1, 1)); // faltam zeros
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "1060", 1, 1)); // sem ':'
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "24:00", 1, 1)); // hora inválida
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "23:60", 1, 1)); // minuto inválido
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "ab:cd", 1, 1)); // não numérico
    }

    @Test
    void mesEDiaInvalidos() {
        ListaTarefas lista = new ListaTarefas();
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "10:00", 0, 1)); // mês 0
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "10:00", 13, 1)); // mês 13
        // fevereiro tem 28
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "10:00", 2, 29));
        // mês de 30 dias
        assertThrows(IllegalArgumentException.class,
                () -> lista.adicionarTarefa("T", "10:00", 4, 31));
    }

    @Test
    void multiplasTarefasMesmoDiaSaoOrdenadasPorHora() {
        ListaTarefas lista = new ListaTarefas();
        lista.adicionarTarefa("Late", "23:00", 6, 10);
        lista.adicionarTarefa("Early", "07:30", 6, 10);
        lista.adicionarTarefa("Noon", "12:15", 6, 10);

        var tarefas = lista.getData().getDia(6, 10).getTarefas();
        assertEquals("07:30", tarefas.get(0).getHora());
        assertEquals("12:15", tarefas.get(1).getHora());
        assertEquals("23:00", tarefas.get(2).getHora());
    }

    @Test
    void toStringIncluiMesELista() {
        ListaTarefas lista = new ListaTarefas();
        lista.adicionarTarefa("X", "09:00", 8, 1);
        String s = lista.toString();
        assertTrue(s.contains("Mês 8"));
        assertTrue(s.contains("09:00 - X"));
    }

}
