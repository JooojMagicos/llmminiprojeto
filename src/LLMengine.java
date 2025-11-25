/**
 * Classe responsável por validar, interpretar e ordenar tarefas
 * através de um LLM externo (ChatGPT, Gemini, etc.).
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Validar strings de entrada fornecidas pelo utilizador.</li>
 *   <li>Converter texto natural em objetos estruturados.</li>
 *   <li>Ordenar listas complexas de tarefas.</li>
 *   <li>Gerar relatórios ou descrições legíveis.</li>
 * </ul>
 */
public class LLMengine {

    /**
     * Verifica se um input textual é válido de acordo com as regras do sistema.
     *
     * @param input string a validar
     * @return true se o LLM considerar válido
     */
    public boolean validarInput(String input) {
        return true; // placeholder
    }

    /**
     * Ordena uma lista textual de tarefas segundo lógica temporal + alfabética.
     *
     * @param lista string contendo tarefas desordenadas
     * @return string com tarefas ordenadas
     */
    public String ordenarTarefas(String lista) {
        return ""; // placeholder
    }

    /**
     * Interpreta texto natural e devolve dados estruturados (JSON).
     *
     * @param descricao descrição informal da tarefa
     * @return JSON contendo título, data e hora
     */
    public String interpretarTarefa(String descricao) {
        return ""; // placeholder
    }
}
