package br.com.sindservferpa.SindServ.Mapper;

import br.com.sindservferpa.SindServ.DTO.AssociadoDTO;
import br.com.sindservferpa.SindServ.DTO.BeneficiarioResponseDTO;
import br.com.sindservferpa.SindServ.DTO.DepDTO;
import br.com.sindservferpa.SindServ.DTO.EnderecoDTO;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.BeneficiarioEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Entity.EnderecoEmbeddable;
import br.com.sindservferpa.SindServ.Repository.BeneficiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

public class Mapper {


    @Autowired
    private BeneficiarioRepository beneficiarioRepository;


    public  AssociadoDTO toAssocDTO(AssociadoEntity assoc) {
        return new AssociadoDTO(
                assoc.getId(),
                assoc.getNome(),
                assoc.getEmail(),
                assoc.getTelefone(),
                assoc.getMatricula(),
                assoc.getCpf(),
                assoc.getData(),
                assoc.getRg(),
                toEnderecoDTO(assoc.getEndereco()),
                assoc.getEstadoCivil(),
                assoc.getConjugue(),
                assoc.getNomeDoPai(),
                assoc.getNomeDaMae(),
                assoc.getEscolaridade(),
                assoc.getLocalDeTrabalho(),
                assoc.getVinculoDeTrabalho(),
                assoc.getCargo(),
                assoc.getDependentes().stream()
                        .map(Mapper::toDepDTO)
                        .collect(Collectors.toList()),
                assoc.getPlano()
        );
    }

    public static BeneficiarioResponseDTO toBeneficiarioRespondeDTO(BeneficiarioEntity beneficiario) {
        BeneficiarioResponseDTO dto = new BeneficiarioResponseDTO();

        dto.setDataInicio(beneficiario.getDataInicio());
        dto.setTitular(beneficiario.getTitular());
        dto.setNomePlano(beneficiario.getPlano().getNome());
        dto.setNomeAssociado(beneficiario.getAssociado().getNome());
        dto.setStatus(beneficiario.getStatus().name());

        if (beneficiario.getDependente() != null) {
            dto.setNomeDependente(beneficiario.getDependente().getNome());
        }



        return dto;
    }

    public static EnderecoDTO toEnderecoDTO(EnderecoEmbeddable endereco) {
        return new EnderecoDTO(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCep()
        );

}

    public static DepDTO toDepDTO(DependenteEntity dep) {
        return new DepDTO(dep.getAssociado().getId(), dep.getNome(), dep.getParentesco(), dep.getData());
    }
}