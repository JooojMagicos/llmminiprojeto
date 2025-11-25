/**
 * Estrutura principal do programa.
 * Contém um objeto {@link Data} que organiza todas as tarefas
 * por mês, dia e hora.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Adicionar tarefas à estrutura correta (mês → dia → hora).</li>
 *   <li>Delegar ao LLM validação ou reorganização de dados textuais.</li>
 *   <li>Fornecer listagens formatadas das tarefas.</li>
 * </ul>
 */
public class ListaTarefas {

    private Data data;

    /** Inicializa a estrutura principal de organização. */
    public ListaTarefas() {
        data = new Data();
    }

    /**
     * Adiciona uma tarefa a um mês e dia específicos.
     *
     * @param titulo título da tarefa
     * @param hora hora no formato HH:mm
     * @param mes número do mês
     * @param dia número do dia
     */
    public void adicionarTarefa(String titulo, String hora, int mes, int dia) {
        Dia dias = data.getDiasDoMes(mes);
        dias.adicionarTarefa(new Tarefa(titulo, hora));
    }

    /** @return estrutura completa de meses e tarefas */
    public Data getData() {
        return data;
    }
}
