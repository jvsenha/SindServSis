package br.com.sindservferpa.SindServ.Controller;


import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.PlanosSaudeDTO;
import br.com.sindservferpa.SindServ.Entity.PlanosSaudeEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Service.PlanosSaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanosConstroller {

    @Autowired
    public  PlanosSaudeService planosSaudeService;

    @PostMapping("/register/plano")
    public ResponseEntity<?> registerPlano(@RequestBody PlanosSaudeDTO dto) {
        try {
            planosSaudeService.registerPlano(dto);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/deletePlano/{id}")
    public ResponseEntity<?> Delete(@PathVariable Long id){
        HashMap<String, String> response = planosSaudeService.deletarPlanosSaude(id);
        return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        try {
            List<PlanosSaudeEntity> lista = planosSaudeService.listarPlano();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id){
        HashMap<String, String> response = planosSaudeService.alterarStatus(id);
        return ResponseEntity.ok(response);
    }
    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterarPlano/{id}")
    public ResponseEntity<?> alterarPlano(@PathVariable Long id, @RequestBody PlanosSaudeDTO dto){
        HashMap<String, String> response = planosSaudeService.alterarPlano(id, dto);
        return ResponseEntity.ok(response);
    }


}
