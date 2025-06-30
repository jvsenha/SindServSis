package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioComDependenteDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioResponseDTO;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import br.com.sindservferpa.SindServ.Service.BeneficiarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/beneficiario")
@Validated
public class BeneficiarioController {

    @Autowired
    public BeneficiarioService beneficiarioService;
    @Autowired
    public DependenteRepository dependenteRepository;


    @PostMapping("/register")
    public ResponseEntity<?> registerBeneficiario(@Valid @RequestBody BeneficiarioComDependenteDTO dados) {
        try {
            Long associadoId = dados.getBeneficiario().getAssociadoId();
            String nome = dados.getNomeDependente();

            if (dados.getNomeDependente() != null) {
                DependenteEntity dependente = dependenteRepository.findByNomeAndAssociadoId(nome, associadoId)
                        .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));
                dados.getBeneficiario().setDependenteId(dependente.getId());
            }

            beneficiarioService.registerBeneficiario(dados.getBeneficiario(), null); // null pois já existe

            return ResponseEntity.ok(dados);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> Delete(@PathVariable Long id) {
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
    public ResponseEntity<?> alterar(@PathVariable Long id) {
        HashMap<String, String> response = beneficiarioService.alterarStatus(id);
        return ResponseEntity.ok(response);
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterar/{id}")
    public ResponseEntity<?> alterarBeneficiario(@PathVariable Long id, @Valid @RequestBody BeneficiarioDTO dto) {
        HashMap<String, String> response = beneficiarioService.alterarBeneficiario(id, dto);
        return ResponseEntity.ok(response);
    }

}
