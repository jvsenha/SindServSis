package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.*;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Repository.AssocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroAssociadoService {

    @Autowired
    private AuthService authService;

    @Autowired
    private BeneficiarioService beneficiarioService;

    @Autowired
    private AssocRepository assocRepository;

    @Transactional
    public AssociadoDTO cadastrarTudo(RegistroAssociadoCompletoDTO dto) {
        // 1. Salvar associado e dependentes
        AssociadoDTO assoc = authService.RegisterAssoc(dto.getAssociado());

        assoc.getDependentes().forEach(d -> d.setAssociadoId(assoc.getId()));

        // 2. Se houver plano e beneficiários
        if (Boolean.TRUE.equals(dto.getAssociado().getPlano()) && dto.getBeneficiariosComDependentes() != null) {

            AssociadoEntity associadoSalvo = assocRepository.findById(assoc.getId())
                    .orElseThrow(() -> new RuntimeException("Associado salvo não encontrado"));

            List<DependenteEntity> dependentesSalvos = associadoSalvo.getDependentes();
            int dependenteIdx = 0;

            for (BeneficiarioComDependenteDTO dados : dto.getBeneficiariosComDependentes()) {
                BeneficiarioDTO b = dados.getBeneficiario();
                b.setAssociadoId(assoc.getId());

                if (Boolean.TRUE.equals(b.getTitular())) {
                    // Associado é titular
                    b.setDependenteId(null);
                    beneficiarioService.registerBeneficiario(b, null);
                } else {
                    // Pega dependente correspondente pela ordem
                    if (dependenteIdx >= dependentesSalvos.size()) {
                        throw new RuntimeException("Número de beneficiários não titulares maior que o de dependentes");
                    }

                    DependenteEntity dep = dependentesSalvos.get(dependenteIdx++);
                    b.setDependenteId(dep.getId());
                    b.setDataNasc(dep.getDataNasc());
                    beneficiarioService.registerBeneficiario(b, convertDepToDepDTO(dep));
                }
            }
        }

        return assoc;
    }

    private DepDTO convertDepToDepDTO(DependenteEntity dep) {
        return new DepDTO(dep.getAssociado().getId(), dep.getNome(), dep.getParentesco(), dep.getDataNasc());
    }
}