package net.javaguides.springboot.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;

@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name =  "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username")
	private String username;

	@Column
	private String email;

	@Column(name = "password")
//	@JsonIgnore
	private String password;


	/* CODE FROM YOUTUBE */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
//	@OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
	@JoinTable(
			name = "member_projects",
			joinColumns = @JoinColumn(
		            name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
				            name = "project_id", referencedColumnName = "id"))
	private Set<Project> projects;

	/* * * * */

	/* previous code */
//	@ManyToMany(mappedBy = "members")
//	private Set<Project> projects = new HashSet<>();



	
	public User() {
		
	}

	public <T> User(String username, String encode, String email) {
		this.username = username;
		this.password = encode;
		this.email = email;
	}

    public User(String username) {
		this.username = username;
    }

	public User(String username, String encode) {
		this.username = username;
		this.password = encode;
	}
}
