package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

import javax.swing.*;

@Data
public class LoginRequest {
    private String email;
    private String senha;
}
