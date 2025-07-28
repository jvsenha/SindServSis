package br.com.sindservferpa.SindServ.Controller;


import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.PlanosSaudeDTO;
import br.com.sindservferpa.SindServ.Entity.PlanosSaudeEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Response.ApiResponse;
import br.com.sindservferpa.SindServ.Service.PlanosSaudeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
@Validated
public class PlanosConstroller {

    @Autowired
    public PlanosSaudeService planosSaudeService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerPlano(@Valid @RequestBody PlanosSaudeDTO dto) {
        try {
            PlanosSaudeEntity plano = planosSaudeService.registerPlano(dto);
            ApiResponse<?> response = new ApiResponse<>(true, "Cadastro Realizado com Sucesso", plano);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao cadastrar", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> Delete(@PathVariable Long id) {
        try {
            planosSaudeService.deletarPlanosSaude(id);
            ApiResponse<?> response = new ApiResponse<>(true, "Cadastro Realizado com Sucesso", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao cadastrar", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<?>> listar() {
            List<PlanosSaudeEntity> lista = planosSaudeService.listarPlano();
            ApiResponse<?> response = new ApiResponse<>(true, "Lista de planos realizada com Sucesso", lista);
            return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id) {
      Status status = planosSaudeService.alterarStatus(id);
        ApiResponse<?> response = new ApiResponse<>(true, "status alterado com Sucesso", status);
        return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterar/{id}")
    public  ResponseEntity<ApiResponse<?>>  alterarPlano(@PathVariable Long id, @Valid @RequestBody PlanosSaudeDTO dto) {
        PlanosSaudeEntity plano = planosSaudeService.alterarPlano(id, dto);
        ApiResponse<?> response = new ApiResponse<>(true, "Plano alterado com Sucesso", plano);
        return ResponseEntity.ok(response);
    }



}
