package br.com.sindservferpa.SindServ.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dependente")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DependenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomeDependete", nullable = false)
    @NotBlank
    private String nome;

    @NotBlank
    @Column(name = "parentesco", nullable = false)
    private String parentesco;

    @ManyToOne
    @JoinColumn(name = "associado_id", nullable = false)
    private AssociadoEntity associado;
}
