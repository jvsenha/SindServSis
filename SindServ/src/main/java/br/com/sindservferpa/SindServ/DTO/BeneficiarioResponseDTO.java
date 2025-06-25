package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BeneficiarioResponseDTO {
    private Long id;
    private LocalDate dataInicio;
    private Boolean titular;
    private String nomePlano;
    private String nomeAssociado;
    private String nomeDependente;
    private String status;
}