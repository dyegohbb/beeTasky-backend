package br.beehome.beetasky.repository.specifications;

import static io.micrometer.common.util.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import br.beehome.beetasky.dto.TaskFilterDTO;
import br.beehome.beetasky.entity.Task;
import jakarta.persistence.criteria.Predicate;

@Component
public class TaskSpecification {

    public Specification<Task> filter(final TaskFilterDTO filter, final String userIdentifier) {
	return (root, query, cb) -> {
	    final List<Predicate> predicates = new ArrayList<>();

	    if (isNotBlank(filter.taskIdentifier())) {
		predicates.add(cb.equal(root.get("identifier"), filter.taskIdentifier()));
	    }

	    if (isNotBlank(filter.title())) {
		predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.title().toLowerCase() + "%"));
	    }

	    if (filter.status() != null) {
		predicates.add(cb.equal(root.get("status"), filter.status()));
	    }

	    if (filter.createdOnStartDate() != null && filter.createdOnEndDate() != null) {
		predicates
			.add(cb.between(root.get("createdOn"), filter.createdOnStartDate(), filter.createdOnEndDate()));
	    } else if (filter.createdOnStartDate() != null) {
		predicates.add(cb.greaterThanOrEqualTo(root.get("createdOn"), filter.createdOnStartDate()));
	    } else if (filter.createdOnEndDate() != null) {
		predicates.add(cb.lessThanOrEqualTo(root.get("createdOn"), filter.createdOnEndDate()));
	    }

	    if (filter.deadlineStartDate() != null && filter.deadlineEndDate() != null) {
		predicates.add(cb.between(root.get("deadline"), filter.deadlineStartDate(), filter.deadlineEndDate()));
	    } else if (filter.deadlineStartDate() != null) {
		predicates.add(cb.greaterThanOrEqualTo(root.get("deadline"), filter.deadlineStartDate()));
	    } else if (filter.deadlineEndDate() != null) {
		predicates.add(cb.lessThanOrEqualTo(root.get("deadline"), filter.deadlineEndDate()));
	    }

	    predicates.add(cb.equal(root.get("deleted"), false));

	    predicates.add(cb.equal(root.get("assignedTo").get("identifier"), userIdentifier));

	    return cb.and(predicates.toArray(new Predicate[0]));
	};
    }

}
