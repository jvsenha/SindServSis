package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.Entity.UserEntity;
import br.com.sindservferpa.SindServ.Enum.Status;
import br.com.sindservferpa.SindServ.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public Status AlteraStatus(Long id){

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Status status = user.getStatus();


        if (status == Status.STATUS_ATIVO) {
            user.setStatus(Status.STATUS_INATIVO);
        }else{
            user.setStatus(Status.STATUS_ATIVO);
        }

        userRepository.save(user);

        return status;
    }

    public List<UserEntity> listar() {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException("Nenhum usuário encontrado no sistema.");
        }
        return users;
    }

}
