package br.com.sindservferpa.SindServ.DTO;

import br.com.sindservferpa.SindServ.Enum.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DependenteResponseDTO {
    private Long id;
    private String nome;
    private String nomeAssociado;
    private String parentesco;
    private LocalDate data;
    private Status status;
}
