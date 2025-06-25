package br.com.sindservferpa.SindServ.Repository;

import br.com.sindservferpa.SindServ.Entity.BeneficiarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiarioRepository extends JpaRepository<BeneficiarioEntity, Long> {
}
