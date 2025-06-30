package br.com.sindservferpa.SindServ.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalhesBeneficiarioDTO {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;
    private Boolean titular;
    private String nomePlano;
    private String nomeAssociado;
    private String nomeDependente;
    private String status;
    private Integer idade;
    private Double mensalidade;
}
