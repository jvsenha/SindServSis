package br.com.sindservferpa.SindServ.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


public class DependenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String parentesco;

    @ManyToOne
    @JoinColumn(name = "associado_id", nullable = false)
    private AssociadoEntity associado;
}
