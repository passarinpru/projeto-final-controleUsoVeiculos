
package br.senai.sistema.model;

public enum StatusUtilizacao {

    EM_UTILIZACAO("Em utilização", "warning"),
    DEVOLVIDO("Devolvido", "success");

    private final String descricao;
    private final String cor;

    StatusUtilizacao(String descricao, String cor) {
        this.descricao = descricao;
        this.cor = cor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCor() {
        return cor;
    }
}
