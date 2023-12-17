package br.com.examplo.sistema.usuariocarros.api.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import br.com.examplo.sistema.usuariocarros.api.controller.dto.UserLoginDTO;
import br.com.examplo.sistema.usuariocarros.api.controller.dto.UsuarioDTO;
import br.com.examplo.sistema.usuariocarros.assembler.UsuarioDTOAssembler;
import br.com.examplo.sistema.usuariocarros.domain.UsuarioService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class AuthResource {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioDTOAssembler usuarioAssembler;

    @PostMapping(path = "/signin")
    public ResponseEntity<ObjectResponse> signin(@Valid @RequestBody UserLoginDTO dto, HttpServletResponse response) {
        String token = service.signin(UserLoginDTO.convertToTO(dto));

        String bearerToken = "Bearer " + token;
        response.addHeader("Authorization", bearerToken);
        response.addHeader("access-control-expose-headers", "Authorization");

        ObjectResponse result  = new ObjectResponse();
        result.setContent(bearerToken);

        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UsuarioDTO> me(Principal principal) {

        UsuarioDTO usuario = usuarioAssembler.toModel(service.findByLogin(principal.getName()));

        return ResponseEntity.ok(usuario);
    }

    public static class ObjectResponse {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
