import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private ListaTarefas lista = new ListaTarefas();
    private LLMInteractionEngine engine = new LLMInteractionEngine(
            "https://modelos.ai.ulusofona.pt/v1/completions",
            "sk-KilLpTcctzTTxXInB4_Idg",
            "gpt-4-turbo",
            true
    );

    private ObservableList<Tarefa> tarefasPendentes = FXCollections.observableArrayList();
    private ObservableList<Tarefa> tarefasFeitas = FXCollections.observableArrayList();

    private ListView<Tarefa> listViewPendentes = new ListView<>(tarefasPendentes);
    private ListView<Tarefa> listViewFeitas = new ListView<>(tarefasFeitas);

    private ProgressBar progressoTarefas = new ProgressBar(0);
    private Label progressoLabel = new Label("Progresso: 0%");

    // NOVO: lista de tarefas completas no modelo
    private List<Tarefa> feitas = new ArrayList<>();

    public List<Tarefa> getFeitas() { return feitas; }
    public void adicionarFeita(Tarefa t) { feitas.add(t); }

    @Override
    public void start(Stage stage) {

        // Layout principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Topo: ProgressBar
        VBox topo = new VBox(5, progressoLabel, progressoTarefas);
        progressoTarefas.setPrefWidth(440);
        root.setTop(topo);

        // Formulário de adicionar tarefa
        TextField tituloField = new TextField();
        tituloField.setPromptText("Título");

        TextField horaField = new TextField();
        horaField.setPromptText("HH:mm");

        TextField mesField = new TextField();
        mesField.setPromptText("Mês");

        TextField diaField = new TextField();
        diaField.setPromptText("Dia");

        Button btnAdicionar = new Button("Adicionar Tarefa");
        HBox formAdicionar = new HBox(10, tituloField, horaField, mesField, diaField, btnAdicionar);

        // Botões de ações
        Button btnMarcarFeita = new Button("Marcar como Feita");
        Button btnApagar = new Button("Apagar Tarefa");
        Button btnAtualizar = new Button("Atualizar Lista");
        Button btnPesquisarMes = new Button("Pesquisar por Mês");
        HBox botoes = new HBox(10, btnMarcarFeita, btnApagar, btnAtualizar, btnPesquisarMes);

        // ListViews
        listViewPendentes.setPrefHeight(200);
        listViewFeitas.setPrefHeight(200);

        VBox listas = new VBox(10,
                new Label("Tarefas Pendentes:"), listViewPendentes,
                new Label("Tarefas Feitas:"), listViewFeitas
        );

        VBox centro = new VBox(15, new Label("Adicionar Tarefa:"), formAdicionar, botoes, listas);
        root.setCenter(centro);

        // ----- Ações dos botões -----
        btnAdicionar.setOnAction(e -> {
            try {
                String titulo = tituloField.getText();
                String hora = horaField.getText();
                int mes = Integer.parseInt(mesField.getText());
                int dia = Integer.parseInt(diaField.getText());

                String prompt = "Verifique se a tarefa abaixo é válida.\n" +
                        "Regras: \n" +
                        "- Hora no formato HH:mm\n" +
                        "- Mês 1-12\n" +
                        "- Dia válido para o mês\n" +
                        "- Título não vazio\n" +
                        "Responda 'OK' ou 'ERRO: <campo> inválido'.\n\n" +
                        "Título: " + titulo + "\nHora: " + hora + "\nMês: " + mes + "\nDia: " + dia;

                String jsonResponse = engine.sendPrompt(prompt);
                String resposta = JSONUtils.getJsonString(jsonResponse, "text").trim();

                if (resposta.equalsIgnoreCase("OK")) {
                    lista.adicionarTarefa(titulo, hora, mes, dia);
                    atualizarPendentes();
                    atualizarStatusLLM();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Tarefa adicionada!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Validação falhou: " + resposta);
                    alert.showAndWait();
                }

                tituloField.clear();
                horaField.clear();
                mesField.clear();
                diaField.clear();

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        btnMarcarFeita.setOnAction(e -> marcarComoFeita());
        btnApagar.setOnAction(e -> apagarTarefa());
        btnAtualizar.setOnAction(e -> atualizarPendentes());
        btnPesquisarMes.setOnAction(e -> pesquisarPorMes());

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Gestor de Tarefas");
        stage.show();
    }

    // Atualiza tarefas pendentes, ignorando as que já foram feitas
    private void atualizarPendentes() {
        tarefasPendentes.clear();
        for (int mes = 1; mes <= 12; mes++) {
            List<Tarefa> t = lista.getData().getDia(mes, 1).getTarefas();
            for (Tarefa tarefa : t) {
                if (!getFeitas().contains(tarefa)) {
                    tarefasPendentes.add(tarefa);
                }
            }
        }
    }

    // Marcar tarefa como feita
    private void marcarComoFeita() {
        Tarefa selecionada = listViewPendentes.getSelectionModel().getSelectedItem();
        if (selecionada == null) return;

        tarefasPendentes.remove(selecionada);
        tarefasFeitas.add(selecionada);

        for (int mes = 1; mes <= 12; mes++) {
            List<Tarefa> t = lista.getData().getDia(mes, 1).getTarefas();
            t.remove(selecionada);
        }

        adicionarFeita(selecionada);
        atualizarStatusLLM();
    }

    private void apagarTarefa() {
        Tarefa selecionada = listViewPendentes.getSelectionModel().getSelectedItem();
        if (selecionada == null) return;

        tarefasPendentes.remove(selecionada);

        for (int mes = 1; mes <= 12; mes++) {
            List<Tarefa> t = lista.getData().getDia(mes, 1).getTarefas();
            t.remove(selecionada);
        }
        atualizarStatusLLM();
    }

    private void pesquisarPorMes() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Pesquisar por Mês");
        dialog.setHeaderText("Digite o número do mês (1-12):");
        dialog.setContentText("Mês:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int mes = Integer.parseInt(input);
                if (mes < 1 || mes > 12) return;

                List<Tarefa> tarefasDoMes = lista.getData().getDia(mes, 1).getTarefas();
                if (tarefasDoMes.isEmpty()) return;

                StringBuilder sb = new StringBuilder();
                for (Tarefa t : tarefasDoMes) sb.append(t).append("\n");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tarefas do mês " + mes);
                alert.setHeaderText("Tarefas:");
                TextArea textArea = new TextArea(sb.toString());
                textArea.setEditable(false);
                textArea.setPrefWidth(400);
                textArea.setPrefHeight(300);
                alert.getDialogPane().setContent(textArea);
                alert.showAndWait();
            } catch (NumberFormatException ignored) {}
        });
    }

    private void atualizarStatusLLM() {
        int completas = tarefasFeitas.size();
        int pendentes = tarefasPendentes.size();

        if (completas + pendentes == 0) {
            progressoTarefas.setProgress(0);
            progressoLabel.setText("Nenhuma tarefa cadastrada.");
            return;
        }

        String prompt = "Com base nas tarefas completas (" + completas + ") e pendentes (" + pendentes +
                "), retorne apenas um número decimal entre 0 e 1 representando a porcentagem de tarefas completadas.";

        try {
            String jsonResponse = engine.sendPrompt(prompt);
            String resposta = JSONUtils.getJsonString(jsonResponse, "text").trim();
            double valor = Double.parseDouble(resposta);
            if (valor < 0) valor = 0;
            if (valor > 1) valor = 1;
            progressoTarefas.setProgress(valor);
            progressoLabel.setText(String.format("Progresso: %.0f%%", valor * 100));
        } catch (Exception e) {
            double fallback = completas / (double)(completas + pendentes);
            progressoTarefas.setProgress(fallback);
            progressoLabel.setText(String.format("Progresso: %.0f%% (erro LLM)", fallback * 100));
        }
    }


    public static void main(String[] args) {
        launch();
    }
}
