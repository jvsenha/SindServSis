package br.com.sindservferpa.SindServ.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "associado")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssociadoEntity extends UserEntity {


    @Column(name = "matricula", unique = true, nullable = false)
    @Size(max = 6, message = "A matricula deve ter no maximo 6 digitos, não colocar o digito")
    @NotBlank
    private String matricula;

    @Column(name = "cpf", unique = true, nullable = false)
    @CPF(message = "CPF invalido")
    @NotBlank
    private String cpf;

    @Column(name = "rg", unique = true, nullable = false)
    @NotBlank
    private String rg;

    @Embedded
    @Valid
    private EnderecoEmbeddable endereco;

    @Column(name = "Data", nullable = false)
    @NotNull(message = "A data não pode ser nula")
    private LocalDate data;

    @Column(name = "estadoCivil", nullable = false)
    @NotBlank
    private String estadoCivil;


    @Column(name = "conjugue")
    private String conjugue;

    @Column(name = "nomeDoPai", nullable = false)
    @NotBlank
    private String nomeDoPai;

    @Column(name = "nomeDaMae", nullable = false)
    @NotBlank
    private String nomeDaMae;

    @Column(name = "escolaridade", nullable = false)
    @NotBlank
    private String escolaridade;

    @Column(name = "localDeTrabalho")
    @NotBlank
    private String localDeTrabalho;

    @Column(name = "vinculoDeTrabalho")
    @NotBlank
    private String vinculoDeTrabalho;

    @Column(name = "cargo")
    @NotBlank
    private String cargo;

    @OneToMany(mappedBy = "associado", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DependenteEntity> dependentes = new ArrayList<>();



}
