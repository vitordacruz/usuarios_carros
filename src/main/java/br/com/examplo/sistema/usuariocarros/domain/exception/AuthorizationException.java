package br.com.examplo.sistema.usuariocarros.domain.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthorizationException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public AuthorizationException(String msg) {
        super(msg);
    }

    public AuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
