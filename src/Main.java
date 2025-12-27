import java.util.List;
import java.util.Scanner;

/**
 * Classe principal da aplicação de gestão de tarefas.
 * <p>
 * Disponibiliza um menu de consola que permite:
 * <ul>
 *     <li>Adicionar tarefas</li>
 *     <li>Listar tarefas pendentes</li>
 *     <li>Marcar tarefas como feitas</li>
 *     <li>Listar tarefas feitas</li>
 *     <li>Apagar tarefas</li>
 * </ul>
 * </p>
 * A validação dos dados da tarefa é realizada através de uma LLM.
 */
public class Main {

    /** Chave de acesso à API da LLM */
    static String apiKey = "sk-51lNzxys8gwcmz3uluLVGQ";

    /** URL do endpoint da API da LLM */
    static String url = "https://modelos.ai.ulusofona.pt/v1/completions";

    /** Modelo de linguagem utilizado */
    static String model = "gpt-4-turbo";

    /** Scanner para leitura de dados do utilizador */
    static Scanner sc = new Scanner(System.in);

    /** Lista principal de tarefas */
    static ListaTarefas lista = new ListaTarefas();

    /**
     * Método principal da aplicação.
     * <p>
     * Inicializa o motor de interação com a LLM e apresenta
     * um menu interativo em ciclo até o utilizador escolher sair.
     * </p>
     *
     * @param args argumentos da linha de comandos (não utilizados)
     * @throws Exception caso ocorra erro na comunicação com a LLM
     */
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

    /**
     * Adiciona uma nova tarefa à lista.
     * <p>
     * Solicita ao utilizador os dados da tarefa e envia-os
     * para uma LLM para validação. Caso a validação seja
     * bem-sucedida, a tarefa é adicionada à lista.
     * </p>
     *
     * @param engine motor de interação com a LLM
     * @throws Exception caso ocorra erro na comunicação com a LLM
     */
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

    /**
     * Lista todas as tarefas pendentes.
     * <p>
     * A apresentação é feita através do método {@code toString()}
     * da lista de tarefas.
     * </p>
     */
    private static void listarPendentes() {
        System.out.println("\n--- PENDENTES ---");
        System.out.println(lista);
    }

    // ===========================
    //       OPÇÃO 3: MARCAR
    // ===========================

    /**
     * Marca uma tarefa como feita.
     * <p>
     * O utilizador escolhe o mês e depois seleciona
     * uma tarefa da lista apresentada.
     * </p>
     */
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

    /**
     * Lista as tarefas marcadas como feitas.
     * <p>
     * Atualmente este método não possui implementação.
     * </p>
     */
    private static void listarFeitas() {
        System.out.println("\n--- FEITAS ---");
        // implementar lista de tarefas feitas se existir
    }

    /**
     * Apaga uma tarefa da lista.
     * <p>
     * O utilizador escolhe o mês e seleciona a tarefa
     * que pretende remover.
     * </p>
     */
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
