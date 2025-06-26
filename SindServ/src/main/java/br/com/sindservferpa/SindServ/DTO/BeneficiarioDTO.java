package br.com.sindservferpa.SindServ.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BeneficiarioDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;
    private Boolean titular;
    private Long planoId;
    private Long associadoId;
    private Long dependenteId;
}
