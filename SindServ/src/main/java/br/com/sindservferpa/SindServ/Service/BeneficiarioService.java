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


    @Transactional
    public BeneficiarioResponseDTO registerBeneficiario(BeneficiarioDTO dto, DepDTO depDto) {
        // Criação da entidade beneficiário
        BeneficiarioEntity beneficiario = new BeneficiarioEntity();

        // Setar plano de saúde
        PlanosSaudeEntity plano = planosSaudeRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        beneficiario.setPlano(plano);

        // Setar associado
        AssociadoEntity associado = assocRepository.findById(dto.getAssociadoId())
                .orElseThrow(() -> new RuntimeException("Associado não encontrado"));
        beneficiario.setAssociado(associado);

        // Dados fixos do beneficiário
        beneficiario.setDataInicio(LocalDate.now());
        beneficiario.setTitular(dto.getTitular());
        beneficiario.setStatus(Status.STATUS_ATIVO);

        // Validação e preenchimento com base no tipo (titular ou dependente)
        if (Boolean.TRUE.equals(dto.getTitular())) {
            // Garante que não exista outro titular
            boolean titularJaExiste = beneficiarioRepository.existsByAssociadoIdAndTitular(dto.getAssociadoId(), true);
            if (titularJaExiste) {
                throw new RuntimeException("Já existe um beneficiário titular para este associado.");
            }

            beneficiario.setDataNasc(associado.getDataNasc());

        } else {
            // Tratamento do dependente
            DependenteEntity dependente = (dto.getDependenteId() == null)
                    ? dependenteService.registerDependenteRetorno(depDto)
                    : dependenteRepository.findById(dto.getDependenteId())
                    .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));

            beneficiario.setDependente(dependente);
            beneficiario.setDataNasc(dependente.getDataNasc());
        }

        // Persistência
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Retorno
        return Mapper.toBeneficiarioRespondeDTO(beneficiario);
    }


    @Transactional
    public void deletarBeneficiario(Long id) {
        BeneficiarioEntity beneficiario = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiário não encontrado."));

        if (Boolean.TRUE.equals(beneficiario.getTitular())) {
            // Remove todos os beneficiários do associado (inclusive ele mesmo e dependentes)
            beneficiarioRepository.deleteByAssociado(beneficiario.getAssociado());
        } else {
            // Remove só o beneficiário dependente
            beneficiarioRepository.delete(beneficiario);
        }
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

        beneficiarioRepository.saveAndFlush(beneficiario);
        return beneficiario.getStatus();
    }

    @Transactional
    public BeneficiarioResponseDTO alterarBeneficiario(Long id, BeneficiarioDTO dto) {

        BeneficiarioEntity beneficiario = BuscarBeneficiario(id);
        AssociadoEntity associado = beneficiario.getAssociado(); // já vem carregado

        beneficiario.setDataInicio(dto.getDataInicio());
        beneficiario.setTitular(dto.getTitular());

        PlanosSaudeEntity plano = planosSaudeRepository.findById(dto.getPlanoId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        beneficiario.setPlano(plano);

        if (dto.getTitular()) {
            if (dto.getDataNasc() == null) {
                throw new RuntimeException("Data de nascimento do titular é obrigatória.");
            }

            beneficiario.setDataNasc(dto.getDataNasc());
            beneficiario.setDependente(null);

            // Atualiza o plano de todos os dependentes
            List<BeneficiarioEntity> dependentes = beneficiarioRepository
                    .findByAssociadoIdAndTitularFalse(associado.getId());
            for (BeneficiarioEntity dep : dependentes) {
                dep.setPlano(plano);
            }

        } else {
            if (dto.getDependenteId() == null) {
                throw new RuntimeException("ID do dependente é obrigatório para não titular.");
            }

            DependenteEntity dependente = dependenteRepository.findById(dto.getDependenteId())
                    .orElseThrow(() -> new RuntimeException("Dependente não encontrado"));

            beneficiario.setDependente(dependente);
            beneficiario.setDataNasc(dependente.getDataNasc());
        }

        beneficiarioRepository.saveAndFlush(beneficiario);

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
