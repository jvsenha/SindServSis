package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.DependenteResponseDTO;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Response.ApiResponse;
import br.com.sindservferpa.SindServ.Service.DependenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dependente")
@Validated
public class DependenteController {

    @Autowired
    public DependenteService dependenteService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerDependente(@Valid @RequestBody DepDTO dto) {
        try {
            DependenteResponseDTO dados = dependenteService.registerDependente(dto);
            ApiResponse<?> response = new ApiResponse<>(true, "Cadastro Realizado com Sucesso", dados);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao cadastrar", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> Delete(@PathVariable Long id) {
        try {
            ApiResponse<?> response = new ApiResponse<>(true, "Dependete deletado com sucesso", null);
            return ResponseEntity.ok(response);
        } catch (
                DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Este Dependete está vinculado a outros dados e não pode ser excluído.", null)
            );
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao Deletar", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping("/listar")
    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    public ResponseEntity<ApiResponse<?>> listar() {
        List<DependenteResponseDTO> lista = dependenteService.listarDependente();
        ApiResponse<?> response = new ApiResponse<>(true, "Dependete listados com sucesso", lista);
        return ResponseEntity.ok(response);
    }


    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/alterarStatus/{id}")
    public ResponseEntity<ApiResponse<?>> alterar(@PathVariable Long id) {
        Status status = dependenteService.alterarStatus(id);
        ApiResponse<?> response = new ApiResponse<>(true, "Status altertado com sucesso", status);
        return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<?>> alterarDependente(@PathVariable Long id, @Valid @RequestBody DepDTO dto) {
        DependenteResponseDTO dependente = dependenteService.alterarDependente(id, dto);
        ApiResponse<?> response = new ApiResponse<>(true, "Dependente altertado com sucesso", dependente);
        return ResponseEntity.ok(response);
    }
}
