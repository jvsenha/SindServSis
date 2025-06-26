package br.com.sindservferpa.SindServ.DTO;

import br.com.sindservferpa.SindServ.Enum.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DependenteResponseDTO {
    private Long id;
    private String nome;
    private String nomeAssociado;
    private String parentesco;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;
    private Status status;
}
