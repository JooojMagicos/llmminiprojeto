import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TarefaTest {

    @Test
    void testGetters() {
        Tarefa t = new Tarefa("Estudar", "09:00");

        assertEquals("Estudar", t.getTitulo());
        assertEquals("09:00", t.getHora());
    }

    @Test
    void testToString() {
        Tarefa t = new Tarefa("Estudar", "09:00");

        assertEquals("09:00 - Estudar", t.toString());
    }


    @Test
    void gettersETostring() {
        Tarefa t = new Tarefa("Comprar", "14:45");
        assertEquals("Comprar", t.getTitulo());
        assertEquals("14:45", t.getHora());
        assertEquals("14:45 - Comprar", t.toString());
    }

    @Test
    void aceitaValoresComZeros() {
        Tarefa t = new Tarefa("Z", "00:00");
        assertEquals("00:00", t.getHora());
    }
}
