package pl.jakusz.gameeconomyspring.repo;


import org.springframework.data.repository.CrudRepository;
import pl.jakusz.gameeconomyspring.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByName(String name);
}