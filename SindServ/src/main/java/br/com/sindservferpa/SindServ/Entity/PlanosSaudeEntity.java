package br.com.sindservferpa.SindServ.Entity;

import br.com.sindservferpa.SindServ.Enum.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planos_de_saude")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanosSaudeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotBlank(message = "Registro ANS é obrigatório")
    @Column(name = "registro_ans", nullable = false)
    private String registroANS;

    @Column(name = "coparticipacao", nullable = false)
    private Boolean coparticipacao = false;

    // Relação N:1 - Vários planos pertencem a uma operadora
    @NotNull(message = "A operadora é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operadora_id", nullable = false)
    @JsonBackReference // Evita loop infinito no JSON (parte "filha")
    private OperadoraPlano operadora;

    @OneToMany(mappedBy = "plano", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FaixaEtariaEntity> faixasEtarias = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;
}