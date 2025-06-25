package br.com.sindservferpa.SindServ.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "fichas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FichasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // alterado para minúsculo

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assoc_id")
    private AssociadoEntity associado;

    @Column(nullable = false)
    private YearMonth mesReferente;  // vai usar converter automaticamente

    @Column(nullable = false)
    private LocalDate dataRetirada;

    private String descricao;
}
