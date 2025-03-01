package br.beehome.beetasky.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.beehome.beetasky.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
