package br.com.sindservferpa.SindServ.Repository;

import br.com.sindservferpa.SindServ.Entity.BeneficiarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiarioRepository extends JpaRepository<BeneficiarioEntity, Long> {

    List<BeneficiarioEntity> findByAssociadoId(Long associadoId);
    Boolean existsByAssociadoId(Long associadoId);
    boolean existsByAssociadoIdAndTitular(Long associadoId, Boolean titular);
}
