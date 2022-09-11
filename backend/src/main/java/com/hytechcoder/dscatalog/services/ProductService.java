package com.hytechcoder.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hytechcoder.dscatalog.dto.CategoryDTO;
import com.hytechcoder.dscatalog.dto.ProductDTO;
import com.hytechcoder.dscatalog.entities.Category;
import com.hytechcoder.dscatalog.entities.Product;
import com.hytechcoder.dscatalog.repositories.CategoryRepository;
import com.hytechcoder.dscatalog.repositories.ProductRepository;
import com.hytechcoder.dscatalog.services.exceptions.DatabaseException;
import com.hytechcoder.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
//	Método para retornar a lista Paginada de todas as categorias
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageResquest){
		Page<Product> list = repository.findAll(pageResquest);
		return list.map(x -> new ProductDTO(x));
	}

	
//	Método para retornar a lista de todas as categorias
//	@Transactional(readOnly = true)
//	public List<ProductDTO> findAll(){
//		List<Product> list = repository.findAll();
//		return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
		
//		List<ProductDTO> listDto = new ArrayList<>();
//		
//		for(Product cat : list) {
//			listDto.add(new ProductDTO(cat));
//		}
		
//		return listDto;
//	}
	
//	método para realizar a busca de uma categoria
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}

//	Método para inserir uma nova categoria
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

//	Método para atualizar uma categoria existente, caso não existe retorna "Id not found concatenado com o id que não existe///////"
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		Product entity = repository.getOne(id);
		
		try {
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);	
			return new ProductDTO(entity);
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

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}

	
	
}
