
package br.senai.sistema.service;

/**
 * Exceção utilizada quando uma regra de negócio
 * do sistema não é respeitada.
 *
 * A mensagem será apresentada ao usuário
 * na tela da aplicação.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
