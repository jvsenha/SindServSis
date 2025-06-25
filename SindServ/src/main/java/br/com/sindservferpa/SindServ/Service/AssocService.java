package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.AssociadoDTO;
import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.EnderecoDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Entity.EnderecoEmbeddable;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Repository.AssocRepository;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import br.com.sindservferpa.SindServ.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Transactional
    public HashMap<String, String> deletarAssociado(Long id) {
        HashMap<String, String> response = new HashMap<>();

        AssociadoEntity associado = BuscarAssoc(id);

        dependenteRepository.deleteById(associado.getId());

        // 2. Deleta o associado
        assocRepository.delete(associado);

        // 3. Deleta o usuário (mesmo ID do associado)
        userRepository.deleteById(id);

        // 4. Mensagem de sucesso
        response.put("mensagem", "Associado, dependentes e usuário deletados com sucesso.");
        return response;
    }

    public AssociadoEntity BuscarAssoc(Long id) {
        return assocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Associado não encontrado."));
    }

    public List<AssociadoEntity> listarAssoc(){
        return assocRepository.findAll();
    }

    public HashMap<String, String> alterarStatus(Long id){
        HashMap<String, String> response = new HashMap<>();

        AssociadoEntity associado = BuscarAssoc(id);

        if(associado.getStatus() == Status.STATUS_ATIVO){
            associado.setStatus(Status.STATUS_INATIVO);
        }else{
            associado.setStatus(Status.STATUS_ATIVO);
        }

        assocRepository.save(associado);

        response.put("mensagem", "Status alterado co sucesso!");
        return response;
    }

    @Transactional
    public HashMap<String, String> alterarAssociado(Long id, AssociadoDTO dto){
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

        if (associado.getDependentes() != null) {
            associado.getDependentes().clear();
        } else {
            associado.setDependentes(new ArrayList<>());
        }

        if (dto.getDependentes() != null) {
            for (DepDTO depDto : dto.getDependentes()) {
                DependenteEntity dep = new DependenteEntity();
                dep.setNome(depDto.getNome());
                dep.setParentesco(depDto.getParentesco());
                dep.setAssociado(associado);
                associado.getDependentes().add(dep); // adiciona à lista original
            }
        }

        // Salva tudo
        assocRepository.save(associado);

        response.put("mensagem", "Associado alterado com sucesso!");
        return response;
    }

}
