package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String nome;
    private String email;
    private String senha;
    private String status;
    private String telefone;
}
