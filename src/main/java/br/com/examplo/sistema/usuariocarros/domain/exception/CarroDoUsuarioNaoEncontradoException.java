package br.com.examplo.sistema.usuariocarros.domain.exception;

/**
 * Exceção lançada quando o Usuário não possui carro com o ID passado
 *
 */
public class CarroDoUsuarioNaoEncontradoException extends CarroNaoEncontradoException {

    /**
     *
     */
    private static final long serialVersionUID = 5249593846085718381L;


    public static String MSG_CARRO_NAO_ENCONTRADO = "Usuário não possui carro com o código %d";

    public CarroDoUsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public CarroDoUsuarioNaoEncontradoException(Long carroId) {

        this(String.format(MSG_CARRO_NAO_ENCONTRADO, carroId));

    }

}
