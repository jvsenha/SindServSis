package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.DependenteResponseDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Mapper.Mapper;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class DependenteService {

    @Autowired
    public DependenteRepository dependenteRepository;

    @Autowired
    public AssocService assocService;

    public DependenteResponseDTO registerDependente(DepDTO dto){

        DependenteEntity dependente = new DependenteEntity();
        AssociadoEntity associado = assocService.BuscarAssoc(dto.getAssociadoId());

        dependente.setNome(dto.getNome());
        dependente.setDataNasc(dto.getDataNasc());
        dependente.setParentesco(dto.getParentesco());
        dependente.setAssociado(associado);
        dependente.setStatus(Status.STATUS_ATIVO);

        dependenteRepository.saveAndFlush(dependente);

        return Mapper.toDepResponseDTO(dependente);
    }

    @Transactional
    public void deletarDependente(Long id) {
       DependenteEntity dependente =  dependenteRepository.findById(id)
               .orElseThrow(()-> new RuntimeException("Dependente não encontrado"));
       dependenteRepository.delete(dependente);
    }

    public Status alterarStatus(Long id) {

        DependenteEntity dependente = BuscarDependente(id);

        if (dependente.getStatus() == Status.STATUS_ATIVO) {
            dependente.setStatus(Status.STATUS_INATIVO);
        } else {
            dependente.setStatus(Status.STATUS_ATIVO);
        }

        dependenteRepository.saveAndFlush(dependente);

        return dependente.getStatus();
    }

    public DependenteEntity BuscarDependente(Long id) {
        return dependenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dependente não encontrado."));
    }

    public List<DependenteResponseDTO> listarDependente() {
        List<DependenteEntity> dependente = dependenteRepository.findAll();

        return dependente.stream().map(d -> {
            DependenteResponseDTO dto = new DependenteResponseDTO();
            dto.setId(d.getId());
            dto.setData(d.getDataNasc());
            dto.setNome(d.getNome());
            dto.setNomeAssociado(d.getAssociado().getNome());
            dto.setParentesco(d.getParentesco());
            dto.setStatus(d.getStatus());

            return dto;
        }).toList();
    }

    public DependenteResponseDTO alterarDependente(Long id, DepDTO dto){
        HashMap<String, String> response = new HashMap<>();
        DependenteEntity dependente = BuscarDependente(id);
        AssociadoEntity associado = assocService.BuscarAssoc(dto.getAssociadoId());

        dependente.setNome(dto.getNome());
        dependente.setDataNasc(dto.getDataNasc());
        dependente.setParentesco(dto.getParentesco());
        dependente.setAssociado(associado);

        dependenteRepository.saveAndFlush(dependente);

        return Mapper.toDepResponseDTO(dependente);
    }

    public DependenteEntity registerDependenteRetorno(DepDTO dto) {
        DependenteEntity dependente = new DependenteEntity();

        AssociadoEntity associado = assocService.BuscarAssoc(dto.getAssociadoId());

        dependente.setNome(dto.getNome());
        dependente.setDataNasc(dto.getDataNasc());
        dependente.setParentesco(dto.getParentesco());
        dependente.setAssociado(associado);
        dependente.setStatus(Status.STATUS_ATIVO);

        return dependenteRepository.saveAndFlush(dependente);
    }
}