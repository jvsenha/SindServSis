package br.com.sindservferpa.SindServ.Entity;

import br.com.sindservferpa.SindServ.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column( name="nome", unique = true, nullable = false )
    private String nome;

    @Email
    @NotBlank(message = "Email é obrigatorio")
    @Column( name="email", unique = true, nullable = false )
    private String email;

    @NotBlank(message = "Senha é obrigatório")
    @Column( name="senha", nullable = false )
    private String senha;

    @NotBlank
    @Column( name="status", nullable = false )
    private String status;

    @NotBlank(message = "Telefone é obrigatorio")
    @Column(name = "telefone", unique = true, nullable = false)
    @Pattern( regexp = "\\([1-9]{2}\\)\\s9[0-9]{4}-[0-9]{4}", message = "Telefone deve estar no formato (99) 99999-9999")
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Role role;

}
