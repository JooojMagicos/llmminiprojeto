import java.util.List;
import java.util.Scanner;

public class Main {

    static String apiKey = "sk-51lNzxys8gwcmz3uluLVGQ";
    static String url = "https://modelos.ai.ulusofona.pt/v1/completions";
    static String model = "gpt-4-turbo";

    static Scanner sc = new Scanner(System.in);
    static ListaTarefas lista = new ListaTarefas();

    public static void main(String[] args) throws Exception {

        LLMInteractionEngine engine =
                new LLMInteractionEngine(url, apiKey, model, true);

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Adicionar tarefa");
            System.out.println("2. Listar pendentes");
            System.out.println("3. Marcar como feita");
            System.out.println("4. Listar feitas");
            System.out.println("5. Apagar tarefa");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> adicionar(engine);
                case 2 -> listarPendentes();
                case 3 -> marcarFeita();
                case 4 -> listarFeitas();
                case 5 -> apagar();
                case 0 -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opção inválida");
            }
        }
    }


    // ===========================
    //       OPÇÃO 1: ADICIONAR
    // ===========================
    private static void adicionar(LLMInteractionEngine engine) throws Exception {
        System.out.print("Título: ");
        String titulo = sc.nextLine();

        System.out.print("Hora (HH:mm): ");
        String hora = sc.nextLine();

        System.out.print("Mês: ");
        int mes = sc.nextInt();

        System.out.print("Dia: ");
        int dia = sc.nextInt();
        sc.nextLine();

        // ---------- ENVIA À LLM PARA VALIDAÇÃO ----------
        // ---------- ENVIA À LLM PARA VALIDAÇÃO ----------
        String prompt = "Verifique se a tarefa abaixo é válida.\n" +
                "Regras: \n" +
                "- Hora no formato HH:mm, entre 00:00 e 23:59\n" +
                "- Mês de 1 a 12\n" +
                "- Dia válido para o mês (fevereiro = 28, meses 30/31 corretos)\n" +
                "- Título não vazio\n" +
                "Responda APENAS com 'OK' se válido ou 'ERRO: <campo> inválido' se inválido.\n\n" +
                "Tarefa a validar:\n" +
                "Título: " + titulo + "\n" +
                "Hora: " + hora + "\n" +
                "Mês: " + mes + "\n" +
                "Dia: " + dia;

        String jsonResponse = engine.sendPrompt(prompt);
        String resposta = JSONUtils.getJsonString(jsonResponse, "text").trim();

        if (resposta.equalsIgnoreCase("OK")) {
            lista.adicionarTarefa(titulo, hora, mes, dia);
            System.out.println("Tarefa adicionada com sucesso!");
        } else {
            System.out.println("Validação falhou: " + resposta);
        }
    }

    // ===========================
    //       OPÇÃO 2: LISTAR
    // ===========================
    private static void listarPendentes() {
        System.out.println("\n--- PENDENTES ---");
        System.out.println(lista);
    }

    // ===========================
    //       OPÇÃO 3: MARCAR
    // ===========================
    private static void marcarFeita() {
        System.out.print("Informe o mês: ");
        int mes = sc.nextInt();
        sc.nextLine();

        List<Tarefa> tarefas = lista.getData().getDia(mes, 1).getTarefas();

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa neste mês.");
            return;
        }

        for (int i = 0; i < tarefas.size(); i++)
            System.out.println((i + 1) + ". " + tarefas.get(i));

        System.out.print("Escolha: ");
        int escolha = sc.nextInt();
        sc.nextLine();

        if (escolha < 1 || escolha > tarefas.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Tarefa escolhida = tarefas.remove(escolha - 1);
        System.out.println("Tarefa marcada como feita: " + escolhida.getTitulo());
    }

    private static void listarFeitas() {
        System.out.println("\n--- FEITAS ---");
        // implementar lista de tarefas feitas se existir
    }

    private static void apagar() {
        System.out.print("Informe o mês: ");
        int mes = sc.nextInt();
        sc.nextLine();

        List<Tarefa> tarefas = lista.getData().getDia(mes, 1).getTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa neste mês.");
            return;
        }

        for (int i = 0; i < tarefas.size(); i++)
            System.out.println((i + 1) + ". " + tarefas.get(i));

        System.out.print("Escolha: ");
        int escolha = sc.nextInt();
        sc.nextLine();

        if (escolha < 1 || escolha > tarefas.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Tarefa removida = tarefas.remove(escolha - 1);
        System.out.println("Tarefa removida: " + removida.getTitulo());
    }
}
