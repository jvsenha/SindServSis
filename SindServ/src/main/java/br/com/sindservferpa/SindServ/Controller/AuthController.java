package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.DTO.*;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import br.com.sindservferpa.SindServ.Service.AuthService;
import br.com.sindservferpa.SindServ.Service.BeneficiarioService;
import br.com.sindservferpa.SindServ.Service.CadastroAssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    public BeneficiarioService beneficiarioService;

    @Autowired
    public DependenteRepository dependenteRepository;

    @Autowired
    public CadastroAssociadoService cadastroAssociadoService;

    @PostMapping("/login")// rota para login
    //Função de login recebe o email e a senha que vem do DTO de login
    // instancia um usuario e passa os paramentros para autenticar na service de Autenticação
    // retorna um ResponseEntity, que é todos os tipos de response http
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse user = authService.autenticar(loginRequest);
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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/assoc")
    public ResponseEntity<?> registerAssoc(@RequestBody RegistroAssociadoCompletoDTO dto) {
        try {
            AssociadoEntity assoc = cadastroAssociadoService.cadastrarTudo(dto);
            return ResponseEntity.ok(assoc);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/register/assoc/lote")
    public ResponseEntity<?> cadastrarEmLote(@RequestBody List<AssociadoDTO> dtos) {
        System.out.println("Quantidade recebida no backend: " + dtos.size());
        List<String> cadastrados = new ArrayList<>();
        List<String> ignorados = new ArrayList<>();

        for (AssociadoDTO dto : dtos) {
            try {
                boolean criado = authService.registerAssocIfNotExists(dto);
                if (criado) {
                    cadastrados.add(dto.getEmail());
                } else {
                    ignorados.add(dto.getEmail());
                }
            } catch (Exception e) {
                // Registra erro individual
                ignorados.add(dto.getEmail() + " (erro: " + e.getMessage() + ")");
                System.out.println("Erro ao cadastrar: " + dto.getEmail() + " -> " + e.getMessage());
            }
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("totalRecebidos", dtos.size());
        resposta.put("cadastrados", cadastrados);
        resposta.put("ignorados", ignorados);

        return ResponseEntity.ok(resposta);
    }


}
