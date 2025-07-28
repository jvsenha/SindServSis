package br.com.sindservferpa.SindServ.Repository;

import br.com.sindservferpa.SindServ.Entity.DependenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface DependenteRepository extends JpaRepository<DependenteEntity, Long> {
    Optional<DependenteEntity> findByNomeAndAssociadoId(String nome, Long associadoId);
    List<DependenteEntity> findByAssociadoId(Long associadoId);
    @Modifying
    int deleteByAssociadoId(Long id);
}
