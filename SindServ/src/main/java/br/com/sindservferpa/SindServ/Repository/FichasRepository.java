package br.com.sindservferpa.SindServ.Repository;

import br.com.sindservferpa.SindServ.Entity.AssociadoEntity;
import br.com.sindservferpa.SindServ.Entity.FichasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;

public interface FichasRepository extends JpaRepository<FichasEntity, Long> {
    boolean existsByAssociadoAndMesReferente(AssociadoEntity associado, YearMonth mesReferente);

}
