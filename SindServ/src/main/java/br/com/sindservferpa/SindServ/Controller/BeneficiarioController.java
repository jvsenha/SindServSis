package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioResponseDTO;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Service.BeneficiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/beneficiario")
public class BeneficiarioController {

    @Autowired
    public BeneficiarioService beneficiarioService;

    @PostMapping("/register/beneficiario")
    public ResponseEntity<?> registerBeneficiario(@RequestBody BeneficiarioDTO dto) {
        try {
            beneficiarioService.registerBeneficiario(dto);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/deleteBeneficiario/{id}")
    public ResponseEntity<?> Delete(@PathVariable Long id){
        HashMap<String, String> response = beneficiarioService.deletarBeneficiario(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar")
    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    public ResponseEntity<?> listar() {
        try {
            List<BeneficiarioResponseDTO> lista = beneficiarioService.listarBeneficiario();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id){
        HashMap<String, String> response = beneficiarioService.alterarStatus(id);
        return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterarBeneficiario/{id}")
    public ResponseEntity<?> alterarBeneficiario(@PathVariable Long id, @RequestBody BeneficiarioDTO dto){
        HashMap<String, String> response = beneficiarioService.alterarBeneficiario(id, dto);
        return ResponseEntity.ok(response);
    }

}
