package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FaixasEtariasDTO {
    private Integer idadeMin;
    private Integer idadeMax;
    private BigDecimal valorMensalidade;
}
