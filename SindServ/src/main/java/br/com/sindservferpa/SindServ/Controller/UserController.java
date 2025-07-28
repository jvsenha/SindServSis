package br.com.sindservferpa.SindServ.Controller;


import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.Entity.UserEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Response.ApiResponse;
import br.com.sindservferpa.SindServ.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    public UserService userService;

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PostMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id) {
        try {
            Status status  = userService.AlteraStatus(id);
            ApiResponse<?> response = new ApiResponse<>(true, "Status alterado com Sucesso", status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao altera status", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        List<UserEntity> lista = userService.listar();
        ApiResponse<?> response = new ApiResponse<>(true, "Lista de usuarios realizadas com Sucesso", lista);
        return ResponseEntity.ok(response);
    }
}
