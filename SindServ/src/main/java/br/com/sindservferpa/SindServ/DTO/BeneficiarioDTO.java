package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BeneficiarioDTO {

    private LocalDate dataInicio;
    private Boolean titular;
    private Long planoId;
    private Long associadoId;
    private Long dependenteId;
}
