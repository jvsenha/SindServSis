package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.BeneficiarioDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioResponseDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.BeneficiarioEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Entity.PlanosSaudeEntity;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Repository.AssocRepository;
import br.com.sindservferpa.SindServ.Repository.BeneficiarioRepository;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import br.com.sindservferpa.SindServ.Repository.PlanosSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


@Service
public class BeneficiarioService {

    @Autowired
    private BeneficiarioRepository beneficiarioRepository;
    @Autowired
    private PlanosSaudeRepository planosSaudeRepository;
    @Autowired
    private AssocRepository assocRepository;
    @Autowired
    private DependenteRepository dependenteRepository;

    public HashMap<String, String> registerBeneficiario(BeneficiarioDTO dto) {
        HashMap<String, String> response = new HashMap<>();

        BeneficiarioEntity beneficiario = new BeneficiarioEntity();
        beneficiario.setDataInicio(dto.getDataInicio());
        beneficiario.setTitular(dto.getTitular());
        beneficiario.setStatus(Status.STATUS_ATIVO);

        PlanosSaudeEntity plano = planosSaudeRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        beneficiario.setPlano(plano);

        AssociadoEntity associado = assocRepository.findById(dto.getAssociadoId())
                .orElseThrow(() -> new RuntimeException("Associado não encontrado"));
        beneficiario.setAssociado(associado);

        if (dto.getDependenteId() != null) {
            DependenteEntity dependente = dependenteRepository.findById(dto.getDependenteId())
                    .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));
            beneficiario.setDependente(dependente);
        }

        beneficiarioRepository.save(beneficiario);

        response.put("mensagem", "Beneficiário cadastrado com sucesso.");
        return response;
    }

    @Transactional
    public HashMap<String, String> deletarBeneficiario(Long id) {
        HashMap<String, String> response = new HashMap<>();
        BeneficiarioEntity beneficiario = BuscarBeneficiario(id);
        beneficiarioRepository.deleteById(beneficiario.getId());

        response.put("mensagem", "Beneficiário deletado com sucesso.");
        return response;
    }

    public BeneficiarioEntity BuscarBeneficiario(Long id) {
        return beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiário não encontrado."));
    }

    public List<BeneficiarioResponseDTO> listarBeneficiario() {
        List<BeneficiarioEntity> beneficiarios = beneficiarioRepository.findAll();

        return beneficiarios.stream().map(b -> {
            BeneficiarioResponseDTO dto = new BeneficiarioResponseDTO();
            dto.setId(b.getId());
            dto.setDataInicio(b.getDataInicio());
            dto.setTitular(b.getTitular());
            dto.setNomePlano(b.getPlano().getNome());
            dto.setNomeAssociado(b.getAssociado().getNome());
            dto.setStatus(b.getStatus().name());

            if (b.getDependente() != null) {
                dto.setNomeDependente(b.getDependente().getNome());
            }

            return dto;
        }).toList();
    }

    public HashMap<String, String> alterarStatus(Long id) {
        HashMap<String, String> response = new HashMap<>();
        BeneficiarioEntity beneficiario = BuscarBeneficiario(id);

        if (beneficiario.getStatus() == Status.STATUS_ATIVO) {
            beneficiario.setStatus(Status.STATUS_INATIVO);
        } else {
            beneficiario.setStatus(Status.STATUS_ATIVO);
        }

        beneficiarioRepository.save(beneficiario);
        response.put("mensagem", "Status do beneficiário alterado com sucesso!");
        return response;
    }

    @Transactional
    public HashMap<String, String> alterarBeneficiario(Long id, BeneficiarioDTO dto) {
        HashMap<String, String> response = new HashMap<>();
        BeneficiarioEntity beneficiario = BuscarBeneficiario(id);

        beneficiario.setDataInicio(dto.getDataInicio());
        beneficiario.setTitular(dto.getTitular());

        PlanosSaudeEntity plano = planosSaudeRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        beneficiario.setPlano(plano);

        AssociadoEntity associado = assocRepository.findById(dto.getAssociadoId())
                .orElseThrow(() -> new RuntimeException("Associado não encontrado"));
        beneficiario.setAssociado(associado);

        if (dto.getDependenteId() != null) {
            DependenteEntity dependente = dependenteRepository.findById(dto.getDependenteId())
                    .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));
            beneficiario.setDependente(dependente);
        }

        beneficiarioRepository.save(beneficiario);
        response.put("mensagem", "Beneficiário alterado com sucesso!");
        return response;
    }
}
