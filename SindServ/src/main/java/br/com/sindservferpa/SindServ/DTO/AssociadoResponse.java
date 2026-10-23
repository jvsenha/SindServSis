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
public class AssociadoResponse extends UserDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String matricula;
    private String cargo;
    private String localDeTrabalho;
}
