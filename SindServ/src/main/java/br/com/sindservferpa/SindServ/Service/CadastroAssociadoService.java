package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.BeneficiarioComDependenteDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioDTO;
import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.RegistroAssociadoCompletoDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroAssociadoService {

    @Autowired
    private AuthService authService;
    @Autowired private BeneficiarioService beneficiarioService;

    @Transactional
    public AssociadoEntity cadastrarTudo(RegistroAssociadoCompletoDTO dto) {
        // 1. Cadastra o associado e dependentes
        AssociadoEntity assoc = authService.RegisterAssoc(dto.getAssociado());

        // 2. Se tiver plano, cadastrar beneficiários
        if (Boolean.TRUE.equals(dto.getAssociado().getPlano()) && dto.getBeneficiariosComDependentes() != null) {
            for (BeneficiarioComDependenteDTO dados : dto.getBeneficiariosComDependentes()) {
                BeneficiarioDTO b = dados.getBeneficiario();
                b.setAssociadoId(assoc.getId());

                if (Boolean.TRUE.equals(b.getTitular())) {
                    // Associado é titular
                    b.setDependenteId(null);
                    beneficiarioService.registerBeneficiario(b, null);
                } else {
                    // Procurar dependente pelo nome
                    DependenteEntity dep = assoc.getDependentes().stream()
                            .filter(d -> d.getNome().equalsIgnoreCase(dados.getNomeDependente()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Dependente não encontrado: " + dados.getNomeDependente()));

                    b.setDependenteId(dep.getId());
                    beneficiarioService.registerBeneficiario(b, convertDepToDepDTO(dep));
                }
            }
        }

        return assoc;
    }

    private DepDTO convertDepToDepDTO(DependenteEntity dep) {
        DepDTO dto = new DepDTO();
        dto.setNome(dep.getNome());
        dto.setParentesco(dep.getParentesco());
        dto.setData(dep.getData());
        dto.setAssociadoId(dep.getAssociado().getId());
        return dto;
    }
}