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

	public Specification<Task> filter(final TaskFilterDTO filter) {
		return (root, query, cb) -> {
			final List<Predicate> predicates = new ArrayList<>();

			if (isNotBlank(filter.taskIdentifier())) {
				predicates.add(cb.equal(root.get("identifier"), filter.taskIdentifier()));
			}

			if (isNotBlank(filter.title())) {
				predicates.add(cb.like(root.get("title"), "%" + filter.title() + "%"));
			}

			if (filter.status() != null) {
				predicates.add(cb.equal(root.get("status"), filter.status()));
			}

			if (filter.createdOn() != null) {
				predicates.add(cb.equal(root.get("createdOn"), filter.createdOn()));
			}

			if (isNotBlank(filter.userIdentifier())) {
				predicates.add(cb.equal(root.get("assignedTo").get("identifier"), filter.userIdentifier()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
