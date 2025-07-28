package br.com.sindservferpa.SindServ.DTO;

import br.com.sindservferpa.SindServ.Enum.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
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
