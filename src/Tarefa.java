

public class Tarefa {
    private final String titulo;
    private final String hora; // HH:mm

    public Tarefa(String titulo, String hora) {
        this.titulo = titulo;
        this.hora = hora;
    }

    public String getTitulo() { return titulo; }
    public String getHora() { return hora; }

    @Override
    public String toString() {
        return hora + " - " + titulo;
    }
}