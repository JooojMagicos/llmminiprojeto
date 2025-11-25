import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Representa um conjunto de tarefas organizadas por dia.
 * Cada dia contém uma lista de tarefas ordenadas automaticamente por hora.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Armazenar tarefas relativas a um único dia.</li>
 *   <li>Manter as tarefas ordenadas por hora (HH:mm).</li>
 *   <li>Permitir a adição e consulta de tarefas.</li>
 * </ul>
 */
public class Dia {

    private List<Tarefa> tarefas;

    /** Cria um objeto Dias com uma lista vazia de tarefas. */
    public Dia() {
        tarefas = new ArrayList<>();
    }

    /**
     * Adiciona uma tarefa e mantém a ordenação por hora.
     *
     * @param tarefa tarefa a ser adicionada
     */
    public void adicionarTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
        ordenarPorHora();
    }

    /** Ordena as tarefas pela hora (crescente). */
    private void ordenarPorHora() {
        tarefas.sort(Comparator.comparing(Tarefa::getHora));
    }

    /**
     * @return lista ordenada das tarefas deste dia
     */
    public List<Tarefa> getTarefas() {
        return tarefas;
    }
}
