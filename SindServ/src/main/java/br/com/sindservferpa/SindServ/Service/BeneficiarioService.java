package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.BeneficiarioDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioResponseDTO;
import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.DetalhesBeneficiarioDTO;
import br.com.sindservferpa.SindServ.Entity.*;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Mapper.Mapper;
import br.com.sindservferpa.SindServ.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
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

    @Autowired
    private FaixaEtariaRepository faixaEtariaRepository;

    @Autowired
    public DependenteService dependenteService;


    public HashMap<String, String> registerBeneficiario(BeneficiarioDTO dto, DepDTO depDto) {
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

        if (Boolean.TRUE.equals(dto.getTitular())) {
            boolean titularJaExiste = beneficiarioRepository.existsByAssociadoIdAndTitular(dto.getAssociadoId(), true);
            if (titularJaExiste) {
                throw new RuntimeException("Já existe um beneficiário titular para este associado.");
            }

            beneficiario.setDataNasc(associado.getDataNasc());
        } else {
            DependenteEntity dependente;

            if (dto.getDependenteId() == null) {
                dependente = dependenteService.registerDependenteRetorno(depDto);
            } else {
                dependente = dependenteRepository.findById(dto.getDependenteId())
                        .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));
            }

            beneficiario.setDependente(dependente);
            beneficiario.setDataNasc(dependente.getDataNasc());
        }

        beneficiarioRepository.save(beneficiario);
        response.put("mensagem", "Beneficiário cadastrado com sucesso.");
        return response;
    }

    @Transactional
    public void deletarBeneficiario(Long id) {
        BeneficiarioEntity beneficiario = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiário não encontrado."));
        beneficiarioRepository.delete(beneficiario);
    }

    public BeneficiarioEntity BuscarBeneficiario(Long id) {
        return beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiário não encontrado."));
    }

    public List<BeneficiarioResponseDTO> listarBeneficiario() {
        List<BeneficiarioEntity> beneficiarios = beneficiarioRepository.findAll();
        return beneficiarios.stream()
                .map(Mapper::toBeneficiarioRespondeDTO)
                .toList();
    }

    public Status alterarStatus(Long id) {
        BeneficiarioEntity beneficiario = BuscarBeneficiario(id);

        if (beneficiario.getStatus() == Status.STATUS_ATIVO) {
            beneficiario.setStatus(Status.STATUS_INATIVO);
        } else {
            beneficiario.setStatus(Status.STATUS_ATIVO);
        }

        beneficiarioRepository.save(beneficiario);
        return beneficiario.getStatus();
    }

    @Transactional
    public BeneficiarioResponseDTO alterarBeneficiario(Long id, BeneficiarioDTO dto) {

        BeneficiarioEntity beneficiario = BuscarBeneficiario(id);

        beneficiario.setDataInicio(dto.getDataInicio());
        beneficiario.setTitular(dto.getTitular());

        PlanosSaudeEntity plano = planosSaudeRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        beneficiario.setPlano(plano);

        AssociadoEntity associado = assocRepository.findById(dto.getAssociadoId())
                .orElseThrow(() -> new RuntimeException("Associado não encontrado"));
        beneficiario.setAssociado(associado);

        if (dto.getTitular()) {
            if (dto.getDataNasc() == null) {
                throw new RuntimeException("Data de nascimento do titular é obrigatória.");
            }
            beneficiario.setDataNasc(dto.getDataNasc());
            beneficiario.setDependente(null);
        } else {
            if (dto.getDependenteId() == null) {
                throw new RuntimeException("ID do dependente é obrigatório para não titular.");
            }

            DependenteEntity dependente = dependenteRepository.findById(dto.getDependenteId())
                    .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));
            beneficiario.setDependente(dependente);
            beneficiario.setDataNasc(dependente.getDataNasc());
        }

        beneficiarioRepository.save(beneficiario);

        return Mapper.toBeneficiarioRespondeDTO(beneficiario);
    }

    public DetalhesBeneficiarioDTO detalhesBeneficiario(Long id) {
        BeneficiarioEntity beneficiario = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiário não encontrado."));
        List<FaixaEtariaEntity> faixaEtarias = beneficiario.getPlano().getFaixasEtarias();
        int idade = Period.between(beneficiario.getDataNasc(), LocalDate.now()).getYears();
        // Buscar faixa etária correspondente
        List<FaixaEtariaEntity> faixas = beneficiario.getPlano().getFaixasEtarias();
        BigDecimal mensalidade = faixas.stream()
                .filter(f -> idade >= f.getIdadeMin() && idade <= f.getIdadeMax())
                .map(FaixaEtariaEntity::getValorMensalidade)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Mensalidade não encontrada, verifique a idade")); // ou lançar exception, se preferir

        // Nome do dependente (verifica se é nulo)
        String nomeDependente = beneficiario.getDependente() != null
                ? beneficiario.getDependente().getNome()
                : beneficiario.getAssociado().getNome();

        return new DetalhesBeneficiarioDTO(
                beneficiario.getId(),
                beneficiario.getDataInicio(),
                beneficiario.getDataNasc(),
                beneficiario.getTitular(),
                beneficiario.getPlano().getNome(),
                beneficiario.getAssociado().getNome(),
                nomeDependente,
                beneficiario.getStatus(),
                idade,
                mensalidade
        );
    }

}
