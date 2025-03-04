package br.beehome.beetasky.entity;

import java.time.LocalDateTime;

import br.beehome.beetasky.common.TaskStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks", indexes = { @Index(name = "idx_tasks_identifier", columnList = "identifier"),
	@Index(name = "idx_tasks_status", columnList = "status"),
	@Index(name = "idx_tasks_assigned_user_id", columnList = "assigned_user_id") })
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identifier", nullable = false, unique = true)
    private String identifier;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatusEnum status;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id", referencedColumnName = "id", nullable = false)
    private User assignedTo;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_on", nullable = true)
    private LocalDateTime deletedOn;

    @Column(name = "updated_on", nullable = true)
    private LocalDateTime updatedOn;

    @PrePersist
    protected void onCreate() {
	this.createdOn = LocalDateTime.now();
    }

    public void logicDelete() {
	this.deleted = true;
	this.deletedOn = LocalDateTime.now();
	this.updatedOn = LocalDateTime.now();
    }
}
