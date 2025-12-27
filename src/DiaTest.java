import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiaTest {

    @Test
    void testAdicionarTarefaOrdenaPorHora() {
        Dia d = new Dia();

        d.adicionarTarefa(new Tarefa("B", "12:00"));
        d.adicionarTarefa(new Tarefa("A", "09:00"));

        assertEquals("09:00", d.getTarefas().get(0).getHora());
    }

    @Test
    void testGetTarefasRetornaCopia() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("A", "10:00"));

        var list = d.getTarefas();
        list.clear();

        assertEquals(1, d.getTarefas().size());
    }

    @Test
    void testToStringVazio() {
        Dia d = new Dia();
        assertTrue(d.toString().contains("(nenhuma tarefa)"));
    }



    @Test
    void adicionaEOrdenaPorHora() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("TarefaB", "12:30"));
        d.adicionarTarefa(new Tarefa("TarefaA", "09:15"));
        d.adicionarTarefa(new Tarefa("TarefaC", "12:00"));

        List<Tarefa> tarefas = d.getTarefas();
        assertEquals(3, tarefas.size());
        assertEquals("09:15", tarefas.get(0).getHora());
        assertEquals("12:00", tarefas.get(1).getHora());
        assertEquals("12:30", tarefas.get(2).getHora());
    }

    @Test
    void estabilidadeQuandoHorasIguaisPreservaOrdemInsercao() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("Primeiro", "10:00"));
        d.adicionarTarefa(new Tarefa("Segundo", "10:00"));
        d.adicionarTarefa(new Tarefa("Terceiro", "10:00"));

        List<Tarefa> tarefas = d.getTarefas();
        assertEquals("Primeiro", tarefas.get(0).getTitulo());
        assertEquals("Segundo", tarefas.get(1).getTitulo());
        assertEquals("Terceiro", tarefas.get(2).getTitulo());
    }

    @Test
    void getTarefasRetornaCopiaDefensiva() {
        Dia d = new Dia();
        d.adicionarTarefa(new Tarefa("X", "07:00"));
        var lista = d.getTarefas();
        lista.clear(); // modifica a cópia
        assertEquals(1, d.getTarefas().size(), "modificar cópia não deve afetar a interna");
    }


    @Test
    void toStringVazioEComTarefas() {
        Dia d = new Dia();
        assertTrue(d.toString().contains("(nenhuma tarefa)"));

        d.adicionarTarefa(new Tarefa("Y", "08:00"));
        String out = d.toString();
        assertTrue(out.contains("08:00 - Y"));
    }
}

