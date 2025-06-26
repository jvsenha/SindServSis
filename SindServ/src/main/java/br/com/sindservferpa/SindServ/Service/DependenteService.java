package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.DependenteResponseDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class DependenteService {

    @Autowired
    public DependenteRepository dependenteRepository;

    @Autowired
    public AssocService assocService;

    public HashMap<String, String> registerDependente(DepDTO dto){
        HashMap<String, String> response = new HashMap<>();

        DependenteEntity dependente = new DependenteEntity();
        AssociadoEntity associado = assocService.BuscarAssoc(dto.getAssociadoId());

        dependente.setNome(dto.getNome());
        dependente.setData(dto.getData());
        dependente.setParentesco(dto.getParentesco());
        dependente.setAssociado(associado);
        dependente.setStatus(Status.STATUS_ATIVO);

        dependenteRepository.saveAndFlush(dependente);

        response.put("mensagem", "Dependente cadastrado com sucesso");
        return response;
    }

    public HashMap<String, String> deletarDependente(Long id) {
        HashMap<String, String> response = new HashMap<>();
        dependenteRepository.deleteById(id);
        response.put("mensagem", "Dependente deletado com sucesso");
        return response;
    }

    public HashMap<String, String> alterarStatus(Long id) {
        HashMap<String, String> response = new HashMap<>();
        DependenteEntity dependente = BuscarDependente(id);

        if (dependente.getStatus() == Status.STATUS_ATIVO) {
            dependente.setStatus(Status.STATUS_INATIVO);
        } else {
            dependente.setStatus(Status.STATUS_ATIVO);
        }

        dependenteRepository.save(dependente);
        response.put("mensagem", "Status do dependente alterado com sucesso!");
        return response;
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
            dto.setData(d.getData());
            dto.setNome(d.getNome());
            dto.setNomeAssociado(d.getAssociado().getNome());
            dto.setParentesco(d.getParentesco());
            dto.setStatus(d.getStatus());

            return dto;
        }).toList();
    }

    public HashMap<String, String> alterarDependente(Long id, DepDTO dto){
        HashMap<String, String> response = new HashMap<>();
        DependenteEntity dependente = BuscarDependente(id);
        AssociadoEntity associado = assocService.BuscarAssoc(dto.getAssociadoId());

        dependente.setNome(dto.getNome());
        dependente.setData(dto.getData());
        dependente.setParentesco(dto.getParentesco());
        dependente.setAssociado(associado);

        dependenteRepository.saveAndFlush(dependente);

        response.put("mensagem", "Dependente alterado com sucesso");
        return response;
    }
}