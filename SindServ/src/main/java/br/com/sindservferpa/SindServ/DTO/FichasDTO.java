package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class FichasDTO {
    private Long id;
    private Long idAssociado;
    private String nomeAssociado;
    private YearMonth mesReferente;
    private LocalDate dataRetirada;
    private String descricao;
}
