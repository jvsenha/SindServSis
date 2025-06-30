package br.com.sindservferpa.SindServ.Controller;

import br.com.sindservferpa.SindServ.DTO.*;
import br.com.sindservferpa.SindServ.Repository.DependenteRepository;
import br.com.sindservferpa.SindServ.Response.ApiResponse;
import br.com.sindservferpa.SindServ.Service.AuthService;
import br.com.sindservferpa.SindServ.Service.BeneficiarioService;
import br.com.sindservferpa.SindServ.Service.CadastroAssociadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
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
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest loginRequest) {
        HashMap<String, String> result = new HashMap<>();
        try {
            LoginResponse user = authService.autenticar(loginRequest);

            result.put("token", user.getToken());
            ApiResponse<?> response = new ApiResponse<>(true, "Login realizado com sucesso", result);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao Logar", e.getMessage());
            return ResponseEntity.status(400).body(erro);
        }
    }

    @PostMapping("/register/func")
    public ResponseEntity<ApiResponse<?>> registerFunc(@Valid @RequestBody FuncDTO dto) {
        try {
            authService.RegisterFunc(dto);
            ApiResponse<?> response = new ApiResponse<>(true, "Cadastro Realizado com Sucesso", dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao cadastrar", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @PostMapping("/register/assoc")
    public ResponseEntity<ApiResponse<?>> registerAssoc(@Valid @RequestBody RegistroAssociadoCompletoDTO dto) {
        try {
            AssociadoDTO assoc = cadastroAssociadoService.cadastrarTudo(dto);
            ApiResponse<AssociadoDTO> response = new ApiResponse<>(true, "Cadastro Realizado com Sucesso", assoc);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> erro = new ApiResponse<>(false, "Erro ao cadastrar", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }


    @PostMapping("/register/assoc/lote")
    public ResponseEntity<?> cadastrarEmLote(@Valid @RequestBody List<AssociadoDTO> dtos) {
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
