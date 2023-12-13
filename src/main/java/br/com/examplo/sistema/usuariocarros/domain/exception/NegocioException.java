package br.com.examplo.sistema.usuariocarros.domain.exception;

public class NegocioException extends RuntimeException {

    private static final long serialVersionUID = 131255741509868377L;

    private Integer errorCode = null;

    public NegocioException(String mensagem) {
        super(mensagem);
    }

    public NegocioException(String mensagem, Integer errorCode) {
        super(mensagem);
        this.errorCode = errorCode;
    }

    public NegocioException (String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}