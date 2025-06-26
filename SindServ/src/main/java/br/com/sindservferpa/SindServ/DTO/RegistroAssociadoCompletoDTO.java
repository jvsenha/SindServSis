package br.com.sindservferpa.SindServ.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RegistroAssociadoCompletoDTO {
    private AssociadoDTO associado;
    private List<BeneficiarioComDependenteDTO> beneficiariosComDependentes;
}