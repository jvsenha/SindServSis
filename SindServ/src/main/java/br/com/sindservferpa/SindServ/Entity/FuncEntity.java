package br.com.sindservferpa.SindServ.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funcionario")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FuncEntity extends UserEntity{

    @Column(name="funcao", nullable = false)
    @NotBlank
    private String funcao;

    @Column(name="tipoContrato", nullable = false)
    @NotBlank
    private String tipoContrato;

    @Column(name="setor", nullable = false)
    @NotBlank
    private String setor;

}
