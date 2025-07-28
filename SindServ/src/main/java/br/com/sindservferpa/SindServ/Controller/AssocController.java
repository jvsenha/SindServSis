package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.AssociadoDTO;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Response.ApiResponse;
import br.com.sindservferpa.SindServ.Service.AssocService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assoc")
@Validated
public class AssocController {

    @Autowired
    public AssocService assocService;

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        try {
            assocService.deletarAssociado(id);
            ApiResponse<?> response = new ApiResponse<>(true, "Associado deletado com sucesso", null);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Este Associado está vinculado a outros dados e não pode ser excluído.", null)
            );
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao Deletar", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/listar")
    public ResponseEntity<ApiResponse<?>> listar() {
        try {
            List<AssociadoDTO> lista = assocService.listarAssoc();
            ApiResponse<?> response = new ApiResponse<>(true, "Lista de associados carregada com sucesso", lista);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao listar associados", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterarStatus/{id}")
    public ResponseEntity<ApiResponse<?>> alterarStatus(@PathVariable Long id) {
        Status status = assocService.alterarStatus(id);
        ApiResponse<?> response = new ApiResponse<>(true, "Status do associado alterado com sucesso!", status);
        return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<?>> alterarAssoc(@PathVariable Long id, @Valid @RequestBody AssociadoDTO dto) {
        AssociadoDTO result = assocService.alterarAssociado(id, dto);
        ApiResponse<?> response = new ApiResponse<>(true, "Associado alterado com sucesso", result);
        return ResponseEntity.ok(response);
    }
}