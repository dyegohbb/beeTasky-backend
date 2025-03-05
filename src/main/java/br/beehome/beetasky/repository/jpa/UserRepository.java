package br.beehome.beetasky.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.beehome.beetasky.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Query("SELECT u FROM User u WHERE (u.username = :login OR u.email = :login) AND u.deleted = false")
    Optional<User> findByUsernameOrEmail(@Param("login") String login);
    
    boolean existsByUsernameOrEmail(String username, String email);
    
    @Query(" SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END "
    	+ "FROM User u WHERE (u.username = :username OR u.email = :email) "
    	+ "AND u.identifier <> :identifier")
    boolean existsByUsernameOrEmailAndIdentifierNot(String username, String email, String identifier);

    Optional<User> findByIdentifier(String identifier);

}
