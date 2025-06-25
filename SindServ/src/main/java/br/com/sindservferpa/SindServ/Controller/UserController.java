package br.com.sindservferpa.SindServ.Controller;


import br.com.sindservferpa.SindServ.Annotation.RequireRole;
import br.com.sindservferpa.SindServ.Entity.UserEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    public UserService userService;

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @PostMapping("/alterarstatus/{id}")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id) {
        try {
            HashMap<String, String> response = userService.AlteraStatus(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @RequireRole({Role.ROLE_ADMIN, Role.ROLE_FUNC})
    @GetMapping("/listar")
    public ResponseEntity<?> listar(){
        try {
            List<UserEntity> lista = userService.listar();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
