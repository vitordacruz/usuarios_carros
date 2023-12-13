package br.com.examplo.sistema.usuariocarros.api.exceptionhandler;

import java.util.List;
import java.util.stream.Collectors;

import br.com.examplo.sistema.usuariocarros.domain.exception.AuthorizationException;
import br.com.examplo.sistema.usuariocarros.domain.exception.EntidadeNaoEncontradaException;
import br.com.examplo.sistema.usuariocarros.domain.exception.NegocioException;
import br.com.examplo.sistema.usuariocarros.util.ConstantesComum;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@ControllerAdvice
public class ApiExceptionHandler  extends ResponseEntityExceptionHandler {

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. "
            + "Tente novamente e se o problema persistir, entre em contato "
            + "com o administrador do sistema.";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e,
                                                                  WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        String detail = e.getMessage();

        Problema problema = Problema.builder().message(detail).errorCode(ConstantesComum.ERROR_CODE_ENTIDADE_NAO_ENCONTRADA)
                .build();

        return handleExceptionInternal(e, problema, new HttpHeaders(), status, request);

    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String detail = e.getMessage();

        Integer errorCode;

        if (e.getErrorCode() == null) {
            errorCode = ConstantesComum.ERROR_CODE_NEGOCIO;
        } else {
            errorCode = e.getErrorCode();
        }

        Problema problema = Problema.builder()
                .message(detail).errorCode(errorCode)
                .build();

        return handleExceptionInternal(e, problema, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        if (body == null) {
            body = Problema.builder().message(MSG_ERRO_GENERICA_USUARIO_FINAL).errorCode(ConstantesComum.ERROR_CODE_ENTIDADE_NAO_ENCONTRADA)
                    .build();
        } else if (body instanceof String) {
            body = Problema.builder().message((String) body).errorCode(ConstantesComum.ERROR_CODE_ENTIDADE_NAO_ENCONTRADA)
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {

        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente", ex.getRequestURL());

        Problema problema = Problema.builder().
                message(detail).errorCode(ConstantesComum.ERROR_CODE_RECURSO_NAO_ENCONTRADO)
                .build();

        return handleExceptionInternal(ex, problema, headers, status, request);


    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> handleAuthorizationException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String detail = ex.getMessage();

        ex.printStackTrace();

        Problema problem = Problema.builder().message(detail)
                .errorCode(ConstantesComum.ERROR_CODE_NAO_AUTORIZADO)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

        ex.printStackTrace();

        Problema problem = Problema.builder().message(detail)
                .errorCode(ConstantesComum.ERROR_CODE_ENTIDADE_NAO_ENCONTRADA)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);

    }

    protected ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult,
                                                              HttpHeaders headers, HttpStatus status, WebRequest request) {

        String textoErro = messageSource.getMessage("campos.invalidos", null , null) + ":";

        List<String> campos = bindingResult.getAllErrors().stream().map(objectError -> {
            String nome = objectError.getObjectName();
            if (objectError instanceof FieldError ) {
                nome = ((FieldError) objectError).getField();
                String errorMessage = ((FieldError) objectError).getDefaultMessage();
                nome += " (" + errorMessage + ")";
            }
            return nome;
        }).collect(Collectors.toList());

        for (String campo : campos) {
            textoErro += " " + campo + ",";
        }


        Problema problem = Problema.builder().message(textoErro)
                .errorCode(ConstantesComum.ERROR_CODE_CAMPOS_INVALIDOS).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        String detail = messageSource.getMessage("campos.invalidos", null, null);

        Problema problema = Problema.builder().message(detail)
                .errorCode(ConstantesComum.ERROR_CODE_CAMPOS_INVALIDOS).build();

        return handleExceptionInternal(ex, problema, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        String detail = "Invalid fields: ";

        if (ex instanceof UnrecognizedPropertyException) {

            detail = "A propriedade '%s' do corpo da requisição não existe. Corrija ou remova e tente novamente";

        } else if (ex instanceof IgnoredPropertyException) {

            detail = "A propriedade '%s' do corpo da requisição deve ser ignorada. Corrija ou remova e tente novamente";

        }

        Problema problema = Problema.builder().message(detail)
                .errorCode(ConstantesComum.ERROR_CODE_CAMPOS_INVALIDOS_1).build();

        return handleExceptionInternal(ex, problema, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {

        String textoErro = messageSource.getMessage("campos.invalidos", null, null);

        Problema problema = Problema.builder().message(textoErro)
                .errorCode(ConstantesComum.ERROR_CODE_CAMPOS_INVALIDOS_2).build();

        return handleExceptionInternal(ex, problema, headers, status, request);

    }
}
