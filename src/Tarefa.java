/**
 * Representa uma tarefa individual dentro de um dia específico.
 * Contém título e hora associados.
 */
public class Tarefa {

    private String titulo;
    private String hora; // formato HH:mm

    /**
     * Cria uma nova tarefa.
     *
     * @param titulo nome da tarefa
     * @param hora hora no formato HH:mm
     */
    public Tarefa(String titulo, String hora) {
        this.titulo = titulo;
        this.hora = hora;
    }

    /** @return o título da tarefa */
    public String getTitulo() {
        return titulo;
    }

    /** @return hora da tarefa no formato HH:mm */
    public String getHora() {
        return hora;
    }

    @Override
    public String toString() {
        return hora + " - " + titulo;
    }
}
