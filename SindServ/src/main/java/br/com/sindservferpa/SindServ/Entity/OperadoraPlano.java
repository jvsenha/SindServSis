package br.com.sindservferpa.SindServ.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "operadoras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperadoraPlano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotBlank(message = "Registro na ANS é obrigatório")
    @Column(name = "registro_ans", nullable = false, unique = true)
    private String registroANS;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(name = "telefone", nullable = false, unique = true)
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // Relação 1:N - Uma operadora tem vários planos
    @OneToMany(mappedBy = "operadora", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Evita loop infinito no JSON (parte "pai")
    private List<PlanosSaudeEntity> planos = new ArrayList<>();
}