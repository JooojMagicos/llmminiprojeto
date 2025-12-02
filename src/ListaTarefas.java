

public class ListaTarefas {
    private final Data data = new Data();

    public void adicionarTarefa(String titulo, String hora, int mes, int dia) {
        validarParametros(titulo, hora, mes, dia);
        Dia d = data.getDia(mes, dia);
        d.adicionarTarefa(new Tarefa(titulo, hora));
    }

    private void validarParametros(String titulo, String hora, int mes, int dia) {
        if (titulo == null || titulo.trim().isEmpty())
            throw new IllegalArgumentException("Título vazio");

        // Validação simples da hora
        if (!hora.matches("\\d{2}:\\d{2}"))
            throw new IllegalArgumentException("Hora inválida (use HH:mm)");

        String[] partes = hora.split(":");
        int hh = Integer.parseInt(partes[0]);
        int mm = Integer.parseInt(partes[1]);

        if (hh < 0 || hh > 23)
            throw new IllegalArgumentException("Hora inválida (HH deve ser 00–23)");

        if (mm < 0 || mm > 59)
            throw new IllegalArgumentException("Minutos inválidos (mm deve ser 00–59)");

        // Validação simples de mês e dia
        if (mes < 1 || mes > 12)
            throw new IllegalArgumentException("Mês inválido");

        int maxDia = data.getMaxDiasDoMes(mes);

        if (dia < 1 || dia > maxDia)
            throw new IllegalArgumentException("Dia inválido (máximo para o mês " + mes + " é " + maxDia + ")");
    }


    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}