import java.util.HashMap;
import java.util.Map;

public class Data {
    private final HashMap<Integer, Dia> meses = new HashMap<>();

    public Dia getDia(int mes, int dia) {
        Dia diaObj = meses.computeIfAbsent(mes, m -> new Dia());
        return diaObj;
    }

    public Map<Integer, Dia> getMeses() {
        return meses;
    }

    public int getMaxDiasDoMes(int mes) {
        return switch (mes) {
            case 1,3,5,7,8,10,12 -> 31;
            case 4,6,9,11 -> 30;
            case 2 -> 28; // sem considerar anos bissextos, como pediste
            default -> throw new IllegalArgumentException("Mês inválido");
        };
    }


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