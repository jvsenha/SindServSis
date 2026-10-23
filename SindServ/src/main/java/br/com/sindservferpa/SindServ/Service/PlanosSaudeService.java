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
import java.util.List;
@Service
public class PlanosSaudeService {

    @Autowired
    private PlanosSaudeRepository planosSaudeRepository;

    @Autowired
    private FaixaEtariaRepository faixaEtariaRepository;

    public PlanosSaudeEntity registerPlano (PlanosSaudeDTO dto) {
        PlanosSaudeEntity plano = new PlanosSaudeEntity();

        plano.setNome(dto.getNome());
        plano.setCoparticipacao(dto.getCoparticipacao());
        plano.setOperadora(dto.getOperadora());
        plano.setStatus(Status.STATUS_ATIVO);

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
        planosSaudeRepository.saveAndFlush(plano);
        return plano ;
    }

    @Transactional
    public void deletarPlanosSaude(Long id) {

        PlanosSaudeEntity plano = BuscarPlano(id);
        // Deleta faixas primeiro
        faixaEtariaRepository.deleteAll(plano.getFaixasEtarias());
        // Depois deleta o plano
        planosSaudeRepository.deleteById(id);
    }

    public PlanosSaudeEntity BuscarPlano(Long id) {
        return planosSaudeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado."));
    }

    public List<PlanosSaudeEntity> listarPlano() {
        return planosSaudeRepository.findAll();
    }

    public Status alterarStatus(Long id) {
        PlanosSaudeEntity plano = BuscarPlano(id);

        if (plano.getStatus() == Status.STATUS_ATIVO) {
            plano.setStatus(Status.STATUS_INATIVO);
        } else {
            plano.setStatus(Status.STATUS_ATIVO);
        }

        planosSaudeRepository.saveAndFlush(plano);

        return plano.getStatus();
    }

    @Transactional
    public PlanosSaudeEntity alterarPlano(Long id, PlanosSaudeDTO dto) {
        PlanosSaudeEntity plano = BuscarPlano(id);

        plano.setNome(dto.getNome());
        plano.setCoparticipacao(dto.getCoparticipacao());
        plano.setOperadora(dto.getOperadora());

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

        planosSaudeRepository.saveAndFlush(plano);

        return plano;
    }
}
