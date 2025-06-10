package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.*;
import br.com.sindservferpa.SindServ.Entity.*;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Repository.AssocRepository;
import br.com.sindservferpa.SindServ.Repository.FuncRepository;
import br.com.sindservferpa.SindServ.Repository.UserRepository;
import br.com.sindservferpa.SindServ.Util.JwtUtil;
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
    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse autenticar(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequest.getSenha(), user.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        if (user.getStatus() != Status.STATUS_ATIVO) {
            throw new RuntimeException("Usuário inativo");
        }

        String token = jwtUtil.generateToken(user);

        return new LoginResponse( token, user.getEmail(), user.getNome(), user.getRole().name(), user.getId());
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
        Assoc.setData(dto.getData());
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