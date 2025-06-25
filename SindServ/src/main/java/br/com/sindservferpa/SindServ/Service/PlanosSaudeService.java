package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.FaixasEtariasDTO;
import br.com.sindservferpa.SindServ.DTO.PlanosSaudeDTO;
import br.com.sindservferpa.SindServ.Entity.FaixaEtariaEntity;
import br.com.sindservferpa.SindServ.Entity.PlanosSaudeEntity;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Repository.FaixaEtariaRepository;
import br.com.sindservferpa.SindServ.Repository.PlanosSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class PlanosSaudeService {

    @Autowired
    private PlanosSaudeRepository planosSaudeRepository;

    @Autowired
    private FaixaEtariaRepository faixaEtariaRepository;

    public HashMap<String, String> registerPlano(PlanosSaudeDTO dto) {
        HashMap<String, String> response = new HashMap<>();

        PlanosSaudeEntity plano = new PlanosSaudeEntity();

        plano.setNome(dto.getNome());
        plano.setCoparticipacao(dto.getCoparticipacao());
        plano.setOperadora(dto.getOperadora());
        plano.setStatus(Status.STATUS_ATIVO);
        plano.setRegistroANS(dto.getRegistroANS());

        List<FaixaEtariaEntity> faixasEtarias = new ArrayList<>();
        if (dto.getFaixasEtarias() != null) {
            for (FaixasEtariasDTO faixasDto : dto.getFaixasEtarias()) {
                FaixaEtariaEntity faixa = new FaixaEtariaEntity();
                faixa.setIdadeMin(faixasDto.getIdadeMin());
                faixa.setIdadeMax(faixasDto.getIdadeMax());
                faixa.setValorMensalidade(faixasDto.getValorMensalidade());
                faixa.setPlano(plano);
                faixasEtarias.add(faixa);
            }
        }

        plano.setFaixasEtarias(faixasEtarias);
        planosSaudeRepository.save(plano);

        response.put("mensagem", "Plano e faixas etárias cadastrados com sucesso.");
        return response;
    }

    @Transactional
    public HashMap<String, String> deletarPlanosSaude(Long id) {
        HashMap<String, String> response = new HashMap<>();
        PlanosSaudeEntity plano = BuscarPlano(id);

        // Deleta faixas primeiro
        faixaEtariaRepository.deleteAll(plano.getFaixasEtarias());

        // Depois deleta o plano
        planosSaudeRepository.deleteById(id);

        response.put("mensagem", "Plano e faixas etárias deletados com sucesso.");
        return response;
    }

    public PlanosSaudeEntity BuscarPlano(Long id) {
        return planosSaudeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado."));
    }

    public List<PlanosSaudeEntity> listarPlano() {
        return planosSaudeRepository.findAll();
    }

    public HashMap<String, String> alterarStatus(Long id) {
        HashMap<String, String> response = new HashMap<>();

        PlanosSaudeEntity plano = BuscarPlano(id);

        if (plano.getStatus() == Status.STATUS_ATIVO) {
            plano.setStatus(Status.STATUS_INATIVO);
        } else {
            plano.setStatus(Status.STATUS_ATIVO);
        }

        planosSaudeRepository.save(plano);
        response.put("mensagem", "Status alterado com sucesso!");
        return response;
    }
    @Transactional
    public HashMap<String, String> alterarPlano(Long id, PlanosSaudeDTO dto) {
        HashMap<String, String> response = new HashMap<>();
        PlanosSaudeEntity plano = BuscarPlano(id);

        plano.setNome(dto.getNome());
        plano.setCoparticipacao(dto.getCoparticipacao());
        plano.setOperadora(dto.getOperadora());
        plano.setRegistroANS(dto.getRegistroANS());

        // Excluir faixas antigas
        faixaEtariaRepository.deleteAll(plano.getFaixasEtarias());
        plano.getFaixasEtarias().clear();

        if (dto.getFaixasEtarias() != null) {
            for (FaixasEtariasDTO faixasDto : dto.getFaixasEtarias()) {
                FaixaEtariaEntity faixa = new FaixaEtariaEntity();
                faixa.setIdadeMin(faixasDto.getIdadeMin());
                faixa.setIdadeMax(faixasDto.getIdadeMax());
                faixa.setValorMensalidade(faixasDto.getValorMensalidade());
                faixa.setPlano(plano);
                plano.getFaixasEtarias().add(faixa);
            }
        }

        planosSaudeRepository.save(plano);
        response.put("mensagem", "Plano alterado com sucesso!");
        return response;
    }
}
