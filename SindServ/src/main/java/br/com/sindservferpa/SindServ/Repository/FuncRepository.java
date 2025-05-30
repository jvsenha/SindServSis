package br.com.sindservferpa.SindServ.Repository;

import br.com.sindservferpa.SindServ.Entity.FuncEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncRepository  extends JpaRepository<FuncEntity, Long> {
    boolean existsByEmail(String email);
}
