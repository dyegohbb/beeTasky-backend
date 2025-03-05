package br.beehome.beetasky.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", indexes = { @Index(name = "idx_users_username", columnList = "username"),
	@Index(name = "idx_users_email", columnList = "email"),
	@Index(name = "idx_users_identifier", columnList = "identifier") })
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identifier", nullable = false, unique = true)
    private String identifier;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_on")
    private LocalDateTime deletedOn;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
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

    public void activeUser() {
	this.deleted = false;
	this.deletedOn = null;
	this.updatedOn = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return new HashSet<>();
    }

}
