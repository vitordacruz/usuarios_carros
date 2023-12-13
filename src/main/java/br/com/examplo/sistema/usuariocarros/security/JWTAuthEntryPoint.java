package br.com.examplo.sistema.usuariocarros.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().append(json(request));
    }

    private String json(HttpServletRequest request) {
        StringBuilder msgError = new StringBuilder("{\"errorCode\": 401, \"message\": ");

        Object attribute = request.getAttribute("errorMessage");

        if (attribute == null) {
            msgError.append("\"Unauthorized\"}");
        } else {
            msgError.append("\"");
            msgError.append(attribute.toString());
            msgError.append("\"}");
        }
        return msgError.toString();
    }

}
