/**
 * Representa uma tarefa com um título e uma hora associada.
 * <p>
 * A hora é armazenada no formato {@code HH:mm}.
 * </p>
 */
public class Tarefa {

    /** Título da tarefa */
    private final String titulo;

    /** Hora associada à tarefa no formato HH:mm */
    private final String hora; // HH:mm

    /**
     * Construtor da classe Tarefa.
     *
     * @param titulo título da tarefa
     * @param hora hora da tarefa no formato HH:mm
     */
    public Tarefa(String titulo, String hora) {
        this.titulo = titulo;
        this.hora = hora;
    }

    /**
     * Obtém o título da tarefa.
     *
     * @return título da tarefa
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtém a hora da tarefa.
     *
     * @return hora da tarefa no formato HH:mm
     */
    public String getHora() {
        return hora;
    }

    /**
     * Devolve uma representação textual da tarefa.
     *
     * @return string no formato "HH:mm - título"
     */
    @Override
    public String toString() {
        return hora + " - " + titulo;
    }
}
