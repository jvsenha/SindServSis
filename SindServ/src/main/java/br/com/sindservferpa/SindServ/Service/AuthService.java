package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.AssociadoDTO;
import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.EnderecoDTO;
import br.com.sindservferpa.SindServ.DTO.FuncDTO;
import br.com.sindservferpa.SindServ.Entity.*;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Repository.AssocRepository;
import br.com.sindservferpa.SindServ.Repository.FuncRepository;
import br.com.sindservferpa.SindServ.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {


    @Autowired
    public UserRepository userRepository;
    @Autowired
    public FuncRepository funcRepository;
    @Autowired
    public AssocRepository assocRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;

    public UserEntity autenticar(String email, String senha) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        return user;
    }

    public void RegisterFunc(FuncDTO dto){
        if(funcRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email já esta cadastrado");
        }
        FuncEntity func = new FuncEntity();

        //atributos usuario em geral
        func.setNome(dto.getNome());
        func.setEmail(dto.getEmail());
        func.setSenha(passwordEncoder.encode(dto.getSenha()));
        func.setStatus(Status.STATUS_ATIVO);
        func.setRole(Role.ROLE_FUNC);
        func.setTelefone(dto.getTelefone());

        //atributos Func
        func.setFuncao(dto.getFuncao());
        func.setSetor(dto.getSetor());
        func.setTipoContrato(dto.getTipoContrato());

        funcRepository.save(func);

    }

    public void RegisterAssoc(AssociadoDTO dto){
        if(assocRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email já esta cadastrado");
        }
        AssociadoEntity Assoc = new AssociadoEntity();


        //atributos usuario em geral
        Assoc.setNome(dto.getNome());
        Assoc.setEmail(dto.getEmail());
        Assoc.setSenha(passwordEncoder.encode(dto.getSenha()));
        Assoc.setStatus(Status.STATUS_ATIVO);
        Assoc.setRole(Role.ROLE_ASC);
        Assoc.setTelefone(dto.getTelefone());

        EnderecoDTO endDto = dto.getEndereco();
        EnderecoEmbeddable end = new EnderecoEmbeddable();
        //Atributos endereço
        end.setRua(endDto.getRua());
        end.setNumero(endDto.getNumero());
        end.setComplemento(endDto.getComplemento());
        end.setBairro(endDto.getBairro());
        end.setCidade(endDto.getCidade());
        end.setEstado(endDto.getEstado());
        end.setEstado(endDto.getEstado());
        end.setCep(endDto.getCep());

        //atributos Func
        Assoc.setMatricula(dto.getMatricula());
        Assoc.setCpf(dto.getCpf());
        Assoc.setVinculo(dto.getVinculo());
        Assoc.setRg(dto.getRg());
        Assoc.setEndereco(end);
        Assoc.setEstadoCivil(dto.getEstadoCivil());
        Assoc.setConjugue(dto.getConjugue());
        Assoc.setNomeDoPai(dto.getNomeDoPai());
        Assoc.setNomeDaMae(dto.getNomeDaMae());
        Assoc.setEscolaridade(dto.getEscolaridade());
        Assoc.setLocalDeTrabalho(dto.getLocalDeTrabalho());
        Assoc.setVinculoDeTrabalho(dto.getVinculoDeTrabalho());
        Assoc.setCargo(dto.getCargo());


        List<DependenteEntity> dependentes = new ArrayList<>();
        if (dto.getDependentes() != null) {
            for (DepDTO depDto : dto.getDependentes()) {
                DependenteEntity dep = new DependenteEntity();
                dep.setNome(depDto.getNome());
                dep.setParentesco(depDto.getParentesco());
                dep.setAssociado(Assoc); // define a relação reversa
                dependentes.add(dep);
            }
        }

        Assoc.setDependentes(dependentes);


        assocRepository.save(Assoc);

    }
}