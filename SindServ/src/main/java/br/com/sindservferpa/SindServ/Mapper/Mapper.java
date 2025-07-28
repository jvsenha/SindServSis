package br.com.sindservferpa.SindServ.Mapper;

import br.com.sindservferpa.SindServ.DTO.*;
import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.BeneficiarioEntity;
import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import br.com.sindservferpa.SindServ.Entity.EnderecoEmbeddable;
import br.com.sindservferpa.SindServ.Repository.BeneficiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class Mapper {


    @Autowired
    private BeneficiarioRepository beneficiarioRepository;


    public AssociadoDTO toAssocDTO(AssociadoEntity assoc) {
        return new AssociadoDTO(
                assoc.getId(),
                assoc.getNome(),
                assoc.getEmail(),
                assoc.getTelefone(),
                assoc.getMatricula(),
                assoc.getCpf(),
                toEnderecoDTO(assoc.getEndereco()),
                assoc.getDataNasc(),
                assoc.getRg(),
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
        dto.setId(beneficiario.getId());
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
        return new DepDTO(dep.getAssociado().getId(), dep.getNome(), dep.getParentesco(), dep.getDataNasc());
    }

    public static DependenteResponseDTO toDepResponseDTO(DependenteEntity dep) {

        DependenteResponseDTO responseDTO = new DependenteResponseDTO();
        responseDTO.setId(dep.getId());
        responseDTO.setNome(dep.getNome());
        responseDTO.setNome(dep.getAssociado().getNome());
        responseDTO.setParentesco(dep.getParentesco());
        responseDTO.setData(dep.getDataNasc());
        responseDTO.setStatus(dep.getStatus());

        return responseDTO;
    }

}