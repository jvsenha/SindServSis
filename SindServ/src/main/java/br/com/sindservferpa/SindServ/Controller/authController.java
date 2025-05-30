package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.DTO.AssociadoDTO;
import br.com.sindservferpa.SindServ.DTO.FuncDTO;
import br.com.sindservferpa.SindServ.DTO.LoginRequest;
import br.com.sindservferpa.SindServ.Entity.UserEntity;
import br.com.sindservferpa.SindServ.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class authController {

    @Autowired
    private AuthService authService;


    @PostMapping("/login")// rota para login
    //Função de login recebe o email e a senha que vem do DTO de login
    // instancia um usuario e passa os paramentros para autenticar na service de Autenticação
    // retorna um ResponseEntity, que é todos os tipos de response http
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserEntity user = authService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register/func")
    public ResponseEntity<?> registerFunc(@RequestBody FuncDTO dto) {
        try {
            authService.RegisterFunc(dto);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
