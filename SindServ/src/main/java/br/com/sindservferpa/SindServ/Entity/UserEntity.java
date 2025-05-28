package br.com.sindservferpa.SindServ.Entity;

import br.com.sindservferpa.SindServ.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nome é obrigatório")
    private String Name;

    @NotBlank(message = "Email é obrigatorio")
    private String email;

    @NotBlank(message = "Senha é obrigatório")
    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

}
