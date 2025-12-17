import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Representa um conjunto de tarefas associadas a um dia.
 * <p>
 * As tarefas são armazenadas numa lista e mantidas ordenadas
 * por hora.
 * </p>
 */
public class Dia {

    /** Lista de tarefas do dia */
    private final List<Tarefa> tarefas = new ArrayList<>();

    /**
     * Adiciona uma nova tarefa ao dia.
     * <p>
     * Após a inserção, as tarefas são ordenadas por hora.
     * </p>
     *
     * @param tarefa tarefa a adicionar
     */
    public void adicionarTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
        tarefas.sort(Comparator.comparing(Tarefa::getHora));
    }

    /**
     * Obtém a lista de tarefas do dia.
     * <p>
     * É devolvida uma cópia defensiva para evitar modificações externas.
     * </p>
     *
     * @return lista de tarefas do dia
     */
    public List<Tarefa> getTarefas() {
        return new ArrayList<>(tarefas); // cópia defensiva
    }

    /**
     * Devolve uma representação textual das tarefas do dia.
     *
     * @return string formatada com as tarefas ou indicação de lista vazia
     */
    @Override
    public String toString() {
        if (tarefas.isEmpty()) return "    (nenhuma tarefa)\n";
        StringBuilder sb = new StringBuilder();
        for (Tarefa t : tarefas) {
            sb.append("    ").append(t).append("\n");
        }
        return sb.toString();
    }
}
