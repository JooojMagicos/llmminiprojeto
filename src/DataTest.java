import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataTest {

    @Test
    void testGetDiaCriaMesSeNaoExistir() {
        Data data = new Data();
        assertTrue(data.getMeses().isEmpty());

        Dia d = data.getDia(3, 1);

        assertNotNull(d);
        assertEquals(1, data.getMeses().size());
        assertTrue(data.getMeses().containsKey(3));
    }

    @Test
    void testGetMaxDiasDoMes() {
        Data data = new Data();

        assertEquals(31, data.getMaxDiasDoMes(1));
        assertEquals(28, data.getMaxDiasDoMes(2));
        assertEquals(30, data.getMaxDiasDoMes(4));
    }

    @Test
    void testGetMaxDiasDoMesInvalido() {
        Data data = new Data();
        assertThrows(IllegalArgumentException.class, () -> data.getMaxDiasDoMes(13));
    }

    @Test
    void testToString() {
        Data data = new Data();
        data.getDia(1, 1).adicionarTarefa(new Tarefa("Teste", "10:00"));

        String out = data.toString();
        assertTrue(out.contains("Mês 1"));
        assertTrue(out.contains("10:00 - Teste"));
    }



    @Test
    void criaDiaSeNaoExistirEOrdenaMesesNoToString() {
        Data data = new Data();
        // adiciona tarefas em meses 12 e 3 para testar ordenamento do toString
        data.getDia(12, 1).adicionarTarefa(new Tarefa("FimAno", "23:59"));
        data.getDia(3, 1).adicionarTarefa(new Tarefa("Março", "08:00"));

        String out = data.toString();

        // deve conter ambos e mês 3 aparece antes de 12 no toString
        int idxMes3 = out.indexOf("Mês 3");
        int idxMes12 = out.indexOf("Mês 12");
        assertTrue(idxMes3 >= 0 && idxMes12 >= 0 && idxMes3 < idxMes12);
        assertTrue(out.contains("08:00 - Março"));
        assertTrue(out.contains("23:59 - FimAno"));
    }

    @Test
    void getMaxDiasDoMesCasos() {
        Data data = new Data();
        assertEquals(31, data.getMaxDiasDoMes(1));
        assertEquals(28, data.getMaxDiasDoMes(2));
        assertEquals(30, data.getMaxDiasDoMes(4));
        assertEquals(31, data.getMaxDiasDoMes(12));
    }

    @Test
    void getMaxDiasDoMesMesInvalido() {
        Data data = new Data();
        assertThrows(IllegalArgumentException.class, () -> data.getMaxDiasDoMes(0));
        assertThrows(IllegalArgumentException.class, () -> data.getMaxDiasDoMes(13));
    }


    @Test
    void getDiaRetornaMesmoObjetoParaMesmoMes() {
        Data data = new Data();
        Dia d1 = data.getDia(5, 1);
        Dia d2 = data.getDia(5, 1);
        assertSame(d1, d2);
    }
}
