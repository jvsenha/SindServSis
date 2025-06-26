package br.com.sindservferpa.SindServ.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class FichasDTO {
    private Long id;
    private Long idAssociado;
    private String nomeAssociado;
    private YearMonth mesReferente;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataRetirada;
    private String descricao;
}
