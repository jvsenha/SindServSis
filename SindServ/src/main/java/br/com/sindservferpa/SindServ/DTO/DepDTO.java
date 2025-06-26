package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DepDTO {
    private Long associadoId;
    private String nome;
    private String parentesco;
    private LocalDate data;
}
