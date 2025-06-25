package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.FichasDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.FichasEntity;
import br.com.sindservferpa.SindServ.Repository.AssocRepository;
import br.com.sindservferpa.SindServ.Repository.FichasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FichasService {

    @Autowired
    private AssocRepository assocRepository;

    @Autowired
    private FichasRepository fichasRepository;

    public HashMap<String, String> registrar(Long id, YearMonth mesReferente, FichasDTO dto) {
        HashMap<String, String> response = new HashMap<>();

        // Buscar o associado
        AssociadoEntity associado = assocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Associado não encontrado."));

        YearMonth mesAtual = YearMonth.now();

        // Verificar se o mês de referência é anterior ao atual
        if (mesReferente.isBefore(mesAtual)) {
            throw new IllegalArgumentException("Não é permitido retirar ficha de mês passado.");
        }

        // Verificar se já foi retirada ficha nesse mês
        if (fichasRepository.existsByAssociadoAndMesReferente(associado, mesReferente)) {
            throw new IllegalArgumentException("Ficha já retirada para este mês.");
        }

        // Criar e preencher a entidade
        FichasEntity ficha = new FichasEntity();
        ficha.setAssociado(associado);
        ficha.setMesReferente(mesReferente);
        ficha.setDataRetirada(LocalDate.now());
        ficha.setDescricao(dto.getDescricao());

        // Salvar no banco
        fichasRepository.saveAndFlush(ficha);

        response.put("memsagem", "Ficha cadastrada com successo");
        return response;
    }


    public List<FichasDTO> listarFichas() {
        List<FichasEntity> entidades = fichasRepository.findAll();

        return entidades.stream().map(ficha -> {
            FichasDTO dto = new FichasDTO();
            dto.setId(ficha.getId());
            dto.setIdAssociado(ficha.getAssociado().getId());
            dto.setNomeAssociado(ficha.getAssociado().getNome());
            dto.setMesReferente(ficha.getMesReferente());
            dto.setDataRetirada(ficha.getDataRetirada());
            dto.setDescricao(ficha.getDescricao());
            return dto;
        }).collect(Collectors.toList());
    }


    public HashMap<String, String> deletar(Long id) {

        HashMap<String, String> response = new HashMap<>();

        fichasRepository.deleteById(id);

        response.put("mensagem", "ficha deletada com sucesso");
        return response;
    }
}
