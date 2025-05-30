package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

@Data
public class FuncDTO extends UserDTO {
    private String funcao;
    private String tipoContrato;
    private String setor;
}
