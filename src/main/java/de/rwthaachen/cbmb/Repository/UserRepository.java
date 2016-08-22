package de.rwthaachen.cbmb.Repository;


import de.rwthaachen.cbmb.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
