package com.hytechcoder.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hytechcoder.dscatalog.dto.CategoryDTO;
import com.hytechcoder.dscatalog.entities.Category;
import com.hytechcoder.dscatalog.repositories.CategoryRepository;
import com.hytechcoder.dscatalog.services.exceptions.DatabaseException;
import com.hytechcoder.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
//	Método para retornar a lista de todas as categorias
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
//		List<CategoryDTO> listDto = new ArrayList<>();
//		
//		for(Category cat : list) {
//			listDto.add(new CategoryDTO(cat));
//		}
		
//		return listDto;
	}

//	método para realizar a busca de uma categoria
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}

//	Método para inserir uma nova categoria
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

//	Método para atualizar uma categoria existente, caso não existe retorna "Id not found concatenado com o id que não existe///////"
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		Category entity = repository.getOne(id);
		
		try {
			entity.setName(dto.getName());
			entity = repository.save(entity);	
			return new CategoryDTO(entity);
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


	
}
