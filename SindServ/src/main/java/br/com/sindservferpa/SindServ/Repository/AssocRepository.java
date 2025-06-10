package br.com.sindservferpa.SindServ.Repository;

import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssocRepository extends JpaRepository<AssociadoEntity, Long> {
    boolean existsByEmail(String email);
}
