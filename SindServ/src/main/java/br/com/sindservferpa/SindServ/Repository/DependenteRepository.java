package br.com.sindservferpa.SindServ.Repository;

import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DependenteRepository extends JpaRepository<DependenteEntity, Long> {
}
