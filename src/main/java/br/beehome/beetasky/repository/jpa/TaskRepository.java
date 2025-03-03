package br.beehome.beetasky.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.beehome.beetasky.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t WHERE t.identifier = :taskIdentifier AND t.assignedTo.identifier = :userIdentifier AND t.deleted = false")
    Optional<Task> findByTaskIdentifierAndUserIdentifier(@Param("taskIdentifier") String taskIdentifier,
	    @Param("userIdentifier") String userIdentifier);

}
