package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

@Data
public class BeneficiarioComDependenteDTO {
    private BeneficiarioDTO beneficiario;
    private String nomeDependente;
}