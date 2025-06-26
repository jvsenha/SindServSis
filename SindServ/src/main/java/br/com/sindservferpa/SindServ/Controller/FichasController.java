package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.FichasDTO;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Service.FichasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/fichas")
public class FichasController {

    @Autowired
    public FichasService fichasService;

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PostMapping("/cadastrar")
    public ResponseEntity<?> registrarFichas(@RequestBody FichasDTO dto) {
        HashMap<String, String> response = fichasService.registrar(dto.getIdAssociado(), dto.getMesReferente(), dto);
        return ResponseEntity.ok(response);
    }
    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        try {
            List<FichasDTO> lista = fichasService.listarFichas();
            return ResponseEntity.ok(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
 @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
     HashMap<String, String> response = fichasService.deletar(id);
     return ResponseEntity.ok(response);
    }

}
