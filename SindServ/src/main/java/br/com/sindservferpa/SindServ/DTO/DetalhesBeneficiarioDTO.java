package br.com.sindservferpa.SindServ.DTO;

import br.com.sindservferpa.SindServ.Enum.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalhesBeneficiarioDTO {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataNasc;
    private Boolean titular;
    private String nomePlano;
    private String nomeAssociado;
    private String nomeDependente;
    private Status status;
    private Integer idade;
    private BigDecimal mensalidade;
}
