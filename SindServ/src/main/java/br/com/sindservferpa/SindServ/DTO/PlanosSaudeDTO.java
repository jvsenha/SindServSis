package br.com.sindservferpa.SindServ.DTO;

import br.com.sindservferpa.SindServ.Entity.OperadoraPlano;
import lombok.Data;

import java.util.List;

@Data
public class PlanosSaudeDTO {

    private String nome;
    private Boolean coparticipacao;
    private OperadoraPlano operadora;
    private String registroANS;
    private List<FaixasEtariasDTO> faixasEtarias;

}
