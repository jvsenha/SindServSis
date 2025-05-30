package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.DTO.FuncDTO;
import br.com.sindservferpa.SindServ.Entity.FuncEntity;
import br.com.sindservferpa.SindServ.Entity.UserEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Repository.FuncRepository;
import br.com.sindservferpa.SindServ.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    @Autowired
    public UserRepository userRepository;
    @Autowired
    public FuncRepository funcRepository;
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
        func.setStatus(dto.getStatus());
        func.setRole(Role.ROLE_FUNC);
        func.setTelefone(dto.getTelefone());

        //atributos Func
        func.setFuncao(dto.getFuncao());
        func.setSetor(dto.getSetor());
        func.setTipoContrato(dto.getTipoContrato());

        funcRepository.save(func);

    }
}