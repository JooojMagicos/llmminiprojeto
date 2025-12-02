

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Dia {
    private final List<Tarefa> tarefas = new ArrayList<>();

    public void adicionarTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
        tarefas.sort(Comparator.comparing(Tarefa::getHora));
    }

    public List<Tarefa> getTarefas() {
        return new ArrayList<>(tarefas); // cópia defensiva
    }

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