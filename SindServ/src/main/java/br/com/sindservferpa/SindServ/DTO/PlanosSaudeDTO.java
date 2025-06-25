package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PlanosSaudeDTO {

    private String nome;
    private Boolean coparticipacao;
    private String operadora;
    private String registroANS;
    private List<FaixasEtariasDTO> faixasEtarias;

}
