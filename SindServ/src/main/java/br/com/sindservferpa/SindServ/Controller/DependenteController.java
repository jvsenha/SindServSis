package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.DependenteResponseDTO;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Service.DependenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dependente")
public class DependenteController {

    @Autowired
    public DependenteService dependenteService;

    @PostMapping("/register")
    public ResponseEntity<?> registerDependente(@RequestBody DepDTO dto) {
        try {
            dependenteService.registerDependente(dto);
            return ResponseEntity.ok(Map.of("mensagem", "Dependente cadastrado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/deleteDependente/{id}")
    public ResponseEntity<?> Delete(@PathVariable Long id){
        HashMap<String, String> response = dependenteService.deletarDependente(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar")
    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    public ResponseEntity<?> listar() {
        try {
            List<DependenteResponseDTO> lista = dependenteService.listarDependente();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id){
        HashMap<String, String> response = dependenteService.alterarStatus(id);
        return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterarDependente/{id}")
    public ResponseEntity<?> alterarDependente(@PathVariable Long id, @RequestBody DepDTO dto){
        HashMap<String, String> response = dependenteService.alterarDependente(id, dto);
        return ResponseEntity.ok(response);
    }
}
