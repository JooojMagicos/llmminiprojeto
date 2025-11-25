import java.util.HashMap;

/**
 * Estrutura que mapeia meses para objetos {@link Dias}.
 * Cada entrada do mapa corresponde a um mês do ano e contém
 * as tarefas desse mês organizadas por dia e hora.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Armazenar os meses do ano usando um HashMap.</li>
 *   <li>Garantir que cada mês possui sua própria estrutura de Dias.</li>
 *   <li>Fornecer acesso e criação dinâmica de meses quando necessário.</li>
 * </ul>
 */
public class Data {

    /**
     * HashMap que guarda: mês → objeto Dias.
     */
    private HashMap<Integer, Dia> meses;

    /** Cria um novo mapa de meses vazio. */
    public Data() {
        meses = new HashMap<>();
    }

    /**
     * Obtém o objeto Dias correspondente ao mês dado.
     * Se não existir, cria automaticamente.
     *
     * @param mes número do mês (1–12)
     * @return o objeto Dias referente ao mês
     */
    public Dia getDiasDoMes(int mes) {
        meses.putIfAbsent(mes, new Dia());
        return meses.get(mes);
    }

    /**
     * @return o HashMap completo contendo todos os meses registados
     */
    public HashMap<Integer, Dia> getMeses() {
        return meses;
    }
}
