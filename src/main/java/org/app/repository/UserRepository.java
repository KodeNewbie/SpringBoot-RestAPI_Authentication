package org.app.repository;

import java.util.Optional;
import org.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>{

    Optional<User> findByUsernameOrEmail(String username, String email);	
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}
