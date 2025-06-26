package br.com.sindservferpa.SindServ.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AssociadoDTO extends UserDTO {
    private String matricula;
    private String cpf;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;
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
