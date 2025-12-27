/**
 * Classe responsável pela gestão da lista de tarefas.
 * <p>
 * Utiliza a classe {@link Data} para organizar as tarefas
 * por mês e dia.
 * </p>
 */
public class ListaTarefas {

    /** Estrutura principal que armazena as tarefas organizadas por data */
    private final Data data = new Data();

    /**
     * Adiciona uma nova tarefa à lista.
     * <p>
     * Antes de adicionar, os parâmetros são validados para
     * garantir a sua consistência.
     * </p>
     *
     * @param titulo título da tarefa
     * @param hora hora da tarefa no formato HH:mm
     * @param mes mês da tarefa (1 a 12)
     * @param dia dia do mês correspondente
     * @throws IllegalArgumentException caso algum parâmetro seja inválido
     */
    public void adicionarTarefa(String titulo, String hora, int mes, int dia) {
        validarParametros(titulo, hora, mes, dia);
        Dia d = data.getDia(mes, dia);
        d.adicionarTarefa(new Tarefa(titulo, hora));
    }

    /**
     * Valida os parâmetros de uma tarefa.
     * <p>
     * Verifica o título, o formato e valores da hora,
     * bem como a validade do mês e do dia.
     * </p>
     *
     * @param titulo título da tarefa
     * @param hora hora da tarefa no formato HH:mm
     * @param mes mês da tarefa
     * @param dia dia da tarefa
     * @throws IllegalArgumentException caso algum parâmetro seja inválido
     */
    private void validarParametros(String titulo, String hora, int mes, int dia) {
        if (titulo == null || titulo.trim().isEmpty())
            throw new IllegalArgumentException("Título vazio");

        if (!hora.matches("\\d{2}:\\d{2}"))
            throw new IllegalArgumentException("Hora inválida (use HH:mm)");

        String[] partes = hora.split(":");
        int hh = Integer.parseInt(partes[0]);
        int mm = Integer.parseInt(partes[1]);

        if (hh < 0 || hh > 23)
            throw new IllegalArgumentException("Hora inválida (HH deve ser 00–23)");

        if (mm < 0 || mm > 59)
            throw new IllegalArgumentException("Minutos inválidos (mm deve ser 00–59)");

        if (mes < 1 || mes > 12)
            throw new IllegalArgumentException("Mês inválido");

        int maxDia = data.getMaxDiasDoMes(mes);

        if (dia < 1 || dia > maxDia)
            throw new IllegalArgumentException(
                    "Dia inválido (máximo para o mês " + mes + " é " + maxDia + ")"
            );
    }

    /**
     * Obtém a estrutura de dados principal que contém as tarefas.
     *
     * @return objeto {@link Data}
     */
    public Data getData() {
        return data;
    }

    /**
     * Devolve uma representação textual da lista de tarefas.
     *
     * @return string representando todas as tarefas organizadas por data
     */
    @Override
    public String toString() {
        return data.toString();
    }
}
