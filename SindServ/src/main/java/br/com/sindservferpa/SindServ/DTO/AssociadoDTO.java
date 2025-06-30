package br.com.sindservferpa.SindServ.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssociadoDTO extends UserDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
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
    private Boolean plano;

}
