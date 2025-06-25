package br.com.sindservferpa.SindServ.Entity;

import br.com.sindservferpa.SindServ.Enum.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @Column(name = "Data", nullable = false)
    @NotNull(message = "A data não pode ser nula")
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "associado_id", nullable = false)
    @JsonBackReference
    private AssociadoEntity associado;

    @Enumerated(EnumType.STRING)
    private Status status;

}
