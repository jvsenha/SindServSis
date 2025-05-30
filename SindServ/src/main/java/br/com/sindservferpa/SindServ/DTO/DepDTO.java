package br.com.sindservferpa.SindServ.DTO;

import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import lombok.Data;

@Data
public class DepDTO {
    private String nome;
    private String parentesco;
    private AssociadoEntity associado;
}
