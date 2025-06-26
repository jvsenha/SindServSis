package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.AssociadoDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Service.AssocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/assoc")
public class AssocController {

    @Autowired
    public AssocService assocService;

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/deleteAssoc/{id}")
    public ResponseEntity<?> Delete(@PathVariable Long id){
        HashMap<String, String> response = assocService.deletarAssociado(id);
        return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        try {
            List<AssociadoEntity> lista = assocService.listarAssoc();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id){
            HashMap<String, String> response = assocService.alterarStatus(id);
            return ResponseEntity.ok(response);
    }
    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterarAssoc/{id}")
    public ResponseEntity<?> alterarAssoc(@PathVariable Long id, @RequestBody AssociadoDTO dto){
            HashMap<String, String> response = assocService.alterarAssociado(id, dto);
            return ResponseEntity.ok(response);
    }


}
