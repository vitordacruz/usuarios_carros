package br.com.examplo.sistema.usuariocarros.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException {

    /**
     *
     */
    private static final long serialVersionUID = -5894520124668192180L;

    private static final String MSG_USUARIO_NAO_ENCONTRADO = "Usuário não encontrado com o código %d";


    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);

    }

    public UsuarioNaoEncontradoException(Long usuarioId) {

        this(String.format(MSG_USUARIO_NAO_ENCONTRADO, usuarioId));

    }

}
