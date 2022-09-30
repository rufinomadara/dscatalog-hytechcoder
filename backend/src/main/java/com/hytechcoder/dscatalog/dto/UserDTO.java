package com.hytechcoder.dscatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.hytechcoder.dscatalog.entities.User;

public class UserDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//atributos que eu quero que transite informações pelo DTO
	
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	
	Set<RoleDTO> roles = new HashSet<>();
	
	public UserDTO() {}

	//construtor do DTO
	public UserDTO(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	//Convertendo a entidade em DTO
	//this é utilizado para referenciar o atributo do objeto
	//nesse caso não precisa do "this" como não existe ambiguidade, pode ser tirado
	public UserDTO(User entity) {
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role))); //para cada entidade que chegou no método eu insiro na lista de CategoryDTO
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

}
