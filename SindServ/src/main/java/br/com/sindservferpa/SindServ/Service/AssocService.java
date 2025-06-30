package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.AssociadoDTO;
import br.com.sindservferpa.SindServ.DTO.EnderecoDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.BeneficiarioEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Entity.EnderecoEmbeddable;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Mapper.Mapper;
import br.com.sindservferpa.SindServ.Repository.AssocRepository;
import br.com.sindservferpa.SindServ.Repository.BeneficiarioRepository;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import br.com.sindservferpa.SindServ.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssocService {

    @Autowired
    private AssocRepository assocRepository;

    @Autowired
    private DependenteRepository dependenteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public BeneficiarioRepository beneficiarioRepository;

    @Autowired
    private Mapper Mapper;

    @Transactional
    public HashMap<String, String> deletarAssociado(Long id) {
        HashMap<String, String> response = new HashMap<>();

        AssociadoEntity associado = BuscarAssoc(id); // assume que lança exceção se não encontrar

        // 1. Deletar beneficiários associados a esse associado
        List<BeneficiarioEntity> beneficiarios = beneficiarioRepository.findByAssociadoId(id);
        beneficiarioRepository.deleteAll(beneficiarios);

        // 2. Deletar dependentes associados a esse associado
        List<DependenteEntity> dependentes = dependenteRepository.findByAssociadoId(id);
        dependenteRepository.deleteAll(dependentes);

        // 3. Deletar o associado (UserEntity herdado será excluído também, se for o mesmo ID)
        assocRepository.delete(associado);

        response.put("mensagem", "Associado, beneficiários e dependentes deletados com sucesso.");
        return response;
    }

    public AssociadoEntity BuscarAssoc(Long id) {
        return assocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Associado não encontrado."));
    }

    public List<AssociadoDTO> listarAssoc() {

        List<AssociadoEntity> lista = assocRepository.findAll();

        return lista.stream()
                .map(Mapper::toAssocDTO)
                .collect(Collectors.toList());
    }

    public HashMap<String, String> alterarStatus(Long id) {
        HashMap<String, String> response = new HashMap<>();

        AssociadoEntity associado = BuscarAssoc(id);

        if (associado.getStatus() == Status.STATUS_ATIVO) {
            associado.setStatus(Status.STATUS_INATIVO);
        } else {
            associado.setStatus(Status.STATUS_ATIVO);
        }

        assocRepository.save(associado);

        response.put("mensagem", "Status alterado co sucesso!");
        return response;
    }

    @Transactional
    public HashMap<String, String> alterarAssociado(Long id, AssociadoDTO dto) {
        HashMap<String, String> response = new HashMap<>();

        AssociadoEntity associado = BuscarAssoc(id); // nome com inicial minúscula por padrão

        if (!associado.getEmail().equals(dto.getEmail()) && assocRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já está cadastrado");
        }

        // Atualiza dados do associado
        associado.setNome(dto.getNome());
        associado.setEmail(dto.getEmail());
        associado.setSenha(passwordEncoder.encode(dto.getSenha()));
        associado.setTelefone(dto.getTelefone());

        // Atualiza endereço existente (se houver), senão cria novo
        EnderecoDTO endDto = dto.getEndereco();
        EnderecoEmbeddable end = associado.getEndereco() != null ? associado.getEndereco() : new EnderecoEmbeddable();
        end.setRua(endDto.getRua());
        end.setNumero(endDto.getNumero());
        end.setComplemento(endDto.getComplemento());
        end.setBairro(endDto.getBairro());
        end.setCidade(endDto.getCidade());
        end.setEstado(endDto.getEstado());
        end.setCep(endDto.getCep());
        associado.setEndereco(end);

        // Atualiza dados pessoais
        associado.setMatricula(dto.getMatricula());
        associado.setCpf(dto.getCpf());
        associado.setData(dto.getData());
        associado.setRg(dto.getRg());
        associado.setEstadoCivil(dto.getEstadoCivil());
        associado.setConjugue(dto.getConjugue());
        associado.setNomeDoPai(dto.getNomeDoPai());
        associado.setNomeDaMae(dto.getNomeDaMae());
        associado.setEscolaridade(dto.getEscolaridade());
        associado.setLocalDeTrabalho(dto.getLocalDeTrabalho());
        associado.setVinculoDeTrabalho(dto.getVinculoDeTrabalho());
        associado.setCargo(dto.getCargo());
        // Salva tudo
        assocRepository.save(associado);

        response.put("mensagem", "Associado alterado com sucesso!");
        return response;
    }

}
