package pl.jakusz.gameeconomyspring.repo;

import org.springframework.data.repository.CrudRepository;
import pl.jakusz.gameeconomyspring.model.Transfer;

public interface TransferRepository extends CrudRepository<Transfer, Long> {
}
