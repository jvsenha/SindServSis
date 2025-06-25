package br.com.sindservferpa.SindServ.Entity;

import br.com.sindservferpa.SindServ.Enum.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "beneficiario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @NotNull(message = "O status de titular é obrigatório")
    @Column(name = "titular", nullable = false)
    private Boolean titular = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plano_id", nullable = false)
    private PlanosSaudeEntity plano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associado_id")
    private AssociadoEntity associado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependente_id")
    private DependenteEntity dependente;

    @Enumerated(EnumType.STRING)
    private Status status;
}
