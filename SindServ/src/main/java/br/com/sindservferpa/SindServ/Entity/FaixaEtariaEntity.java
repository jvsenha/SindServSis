package br.com.sindservferpa.SindServ.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "faixaEtaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaixaEtariaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A idade mínima é obrigatória")
    @Column(name = "idade_min", nullable = false)
    private Integer idadeMin;

    @NotNull(message = "A idade máxima é obrigatória")
    @Column(name = "idade_max", nullable = false)
    private Integer idadeMax;

    @NotNull(message = "O valor da mensalidade é obrigatório")
    @Column(name = "valor_mensalidade", nullable = false)
    private BigDecimal valorMensalidade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plano_id")
    @JsonBackReference
    private PlanosSaudeEntity plano;
}
