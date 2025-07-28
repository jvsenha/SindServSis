package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioComDependenteDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioResponseDTO;
import br.com.sindservferpa.SindServ.DTO.DetalhesBeneficiarioDTO;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import br.com.sindservferpa.SindServ.Response.ApiResponse;
import br.com.sindservferpa.SindServ.Service.BeneficiarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<?>> registerBeneficiario(@Valid @RequestBody BeneficiarioComDependenteDTO dados) {
        try {
            Long associadoId = dados.getBeneficiario().getAssociadoId();
            String nome = dados.getNomeDependente();

            if (dados.getNomeDependente() != null) {
                DependenteEntity dependente = dependenteRepository.findByNomeAndAssociadoId(nome, associadoId)
                        .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));
                dados.getBeneficiario().setDependenteId(dependente.getId());
            }

            beneficiarioService.registerBeneficiario(dados.getBeneficiario(), null); // null pois já existe

            ApiResponse<?> response = new ApiResponse<>(true, "Cadastro Realizado com Sucesso", dados);
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
            beneficiarioService.deletarBeneficiario(id);
            ApiResponse<?> response = new ApiResponse<>(true, "Beneficiario deletado com sucesso", null);
            return ResponseEntity.ok(response);
        } catch (
                DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Este Beneficiario está vinculado a outros dados e não pode ser excluído.", null)
            );
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao Deletar", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping("/listar")
    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    public ResponseEntity<ApiResponse<List<BeneficiarioResponseDTO>>> listar() {
        List<BeneficiarioResponseDTO> lista = beneficiarioService.listarBeneficiario();
        ApiResponse<List<BeneficiarioResponseDTO>> response =
                new ApiResponse<>(true, "Beneficiários listados com sucesso", lista);
        return ResponseEntity.ok(response);
    }


    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/alterarStatus/{id}")
    public ResponseEntity<ApiResponse<?>> alterar(@PathVariable Long id) {
        try {
            Status status = beneficiarioService.alterarStatus(id);
            ApiResponse<?> response = new ApiResponse<>(true, "Status Alterado com sucesso", status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao altera status", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PutMapping("/alterar/{id}")
    public ResponseEntity<ApiResponse<?>> alterarBeneficiario(@PathVariable Long id, @Valid @RequestBody BeneficiarioDTO dto) {
        try {
            BeneficiarioResponseDTO result = beneficiarioService.alterarBeneficiario(id, dto);
            ApiResponse<?> response = new ApiResponse<>(true, "Beneficiario Alterado com sucesso", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao Alterar beneficiario", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/detalhes/{id}")
    public ResponseEntity<ApiResponse<?>> detalhesBeneficiario(@PathVariable Long id) {
        try {
            DetalhesBeneficiarioDTO detalhes = beneficiarioService.detalhesBeneficiario(id);
            ApiResponse<DetalhesBeneficiarioDTO> response = new ApiResponse<>(true, "Detalhes do beneficiário carregados com sucesso", detalhes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao carregar Detalhes do beneficiário", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }


}
