package com.hytechcoder.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hytechcoder.dscatalog.dto.RoleDTO;
import com.hytechcoder.dscatalog.dto.UserDTO;
import com.hytechcoder.dscatalog.dto.UserInsertDTO;
import com.hytechcoder.dscatalog.entities.Role;
import com.hytechcoder.dscatalog.entities.User;
import com.hytechcoder.dscatalog.repositories.RoleRepository;
import com.hytechcoder.dscatalog.repositories.UserRepository;
import com.hytechcoder.dscatalog.services.exceptions.DatabaseException;
import com.hytechcoder.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEnconder;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
//	Método para retornar a lista Paginada de todas as categorias
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable){
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}
	
//	Método para retornar a lista de todas as categorias
//	@Transactional(readOnly = true)
//	public List<UserDTO> findAll(){
//		List<User> list = repository.findAll();
//		return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
		
//		List<UserDTO> listDto = new ArrayList<>();
//		
//		for(User cat : list) {
//			listDto.add(new UserDTO(cat));
//		}
		
//		return listDto;
//	}
	
//	método para realizar a busca de um Usuário
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

//	Método para inserir uma nova categoria
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEnconder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

//	Método para atualizar uma categoria existente, caso não existe retorna "Id not found concatenado com o id que não existe///////"
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		User entity = repository.getOne(id);
		
		try {
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);	
			return new UserDTO(entity);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}	
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation!");
		}

	}

	private void copyDtoToEntity(UserDTO dto, User entity) {
		
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		entity.getRoles().clear();
		for(RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}
	}

	
	
}
