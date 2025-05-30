package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
public class AssociadoDTO extends UserDTO {
    private String matricula;
    private String cpf;
    private String vinculo;
    private String rg;
    private EnderecoDTO endereco;
    private String estadoCivil;
    private String conjugue;
    private String nomeDoPai;
    private String nomeDaMae;
    private String escolaridade;
    private String localDeTrabalho;
    private String vinculoDeTrabalho;
    private String cargo;
    private List<DepDTO> dependentes;
}
