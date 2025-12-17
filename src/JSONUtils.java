/**
 * Classe utilitária para operações simples sobre strings JSON.
 * <p>
 * Contém métodos auxiliares para formatação básica de JSON
 * e extração simples de valores associados a chaves.
 * </p>
 */
public class JSONUtils {

    /**
     * Formata rapidamente uma string JSON, adicionando indentação
     * e quebras de linha para melhorar a legibilidade.
     * <p>
     * Este método não realiza uma validação completa do JSON
     * e assume que a string de entrada está minimamente bem formada.
     * </p>
     *
     * @param json string JSON a formatar
     * @return string JSON formatada
     */
    static String quickJSONFormater(String json) {
        StringBuilder out = new StringBuilder();
        boolean inStr = false, esc = false;
        int indent = 0;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (esc) { out.append(c); esc = false; continue; }
            if (c == '\\') { out.append(c); esc = true; continue; }
            if (c == '\"') { inStr = !inStr; out.append(c); continue; }
            if (inStr) { out.append(c); continue; }

            switch (c) {
                case '{': case '[':
                    out.append(c).append('\n').append("  ".repeat(++indent));
                    break;
                case '}': case ']':
                    out.append('\n').append("  ".repeat(--indent)).append(c);
                    break;
                case ',':
                    out.append(c).append('\n').append("  ".repeat(indent));
                    break;
                case ':':
                    out.append(": ");
                    break;
                default:
                    if (!Character.isWhitespace(c)) out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * Extrai o valor de um campo JSON simples no formato {@code "chave": "valor"}.
     * <p>
     * Este método é ingênuo e não suporta:
     * <ul>
     *     <li>JSONs aninhados complexos</li>
     *     <li>Múltiplos campos com a mesma chave</li>
     *     <li>Valores que não sejam strings</li>
     * </ul>
     * </p>
     *
     * @param json string JSON de onde será extraído o valor
     * @param key chave a procurar no JSON
     * @return valor associado à chave ou {@code null} se não encontrado
     */
    static String getJsonString(String json, String key) {
        String pattern = "\"" + key + "\"";
        int keyPos = json.indexOf(pattern);
        if (keyPos < 0) return null;

        int colonPos = json.indexOf(':', keyPos + pattern.length());
        if (colonPos < 0) return null;

        int firstQuote = json.indexOf('"', colonPos + 1);
        if (firstQuote < 0) return null;

        int secondQuote = json.indexOf('"', firstQuote + 1);
        while (secondQuote > 0 && json.charAt(secondQuote - 1) == '\\') {
            secondQuote = json.indexOf('"', secondQuote + 1);
        }
        if (secondQuote < 0) return null;

        return json.substring(firstQuote + 1, secondQuote);
    }

}
