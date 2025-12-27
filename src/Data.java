import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por gerir as tarefas organizadas por mês.
 * <p>
 * Cada mês está associado a um objeto {@link Dia}, que contém
 * as tarefas correspondentes.
 * </p>
 */
public class Data {

    /** Estrutura que associa cada mês ao respetivo conjunto de dias */
    private final HashMap<Integer, Dia> meses = new HashMap<>();

    /**
     * Obtém o objeto {@link Dia} correspondente a um determinado mês e dia.
     * <p>
     * Caso o mês ainda não exista, é criado automaticamente.
     * </p>
     *
     * @param mes número do mês (1 a 12)
     * @param dia número do dia do mês
     * @return objeto {@link Dia} correspondente
     */
    public Dia getDia(int mes, int dia) {
        Dia diaObj = meses.computeIfAbsent(mes, m -> new Dia());
        return diaObj;
    }

    /**
     * Obtém o mapa de meses existentes.
     *
     * @return mapa que associa meses a objetos {@link Dia}
     */
    public Map<Integer, Dia> getMeses() {
        return meses;
    }

    /**
     * Devolve o número máximo de dias de um determinado mês.
     * <p>
     * Não considera anos bissextos.
     * </p>
     *
     * @param mes número do mês (1 a 12)
     * @return número máximo de dias do mês
     * @throws IllegalArgumentException caso o mês seja inválido
     */
    public int getMaxDiasDoMes(int mes) {
        return switch (mes) {
            case 1,3,5,7,8,10,12 -> 31;
            case 4,6,9,11 -> 30;
            case 2 -> 28;
            default -> throw new IllegalArgumentException("Mês inválido");
        };
    }

    /**
     * Devolve uma representação textual das tarefas organizadas por mês.
     *
     * @return string com os meses e respetivas tarefas
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        meses.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    int mes = entry.getKey();
                    sb.append("Mês ").append(mes).append(":\n");
                    sb.append(entry.getValue());
                    sb.append("\n");
                });
        return sb.toString();
    }
}
