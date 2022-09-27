package com.hytechcoder.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.hytechcoder.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	@Test
	public void deleteShouldDeleteObjectWhenExists() {
		
		//Arrange
		long exintingId = 1L;
		
		//Act
		repository.deleteById(exintingId);
		
		//Assert
		Optional<Product> result = repository.findById(exintingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		long nonExistingId = 1000L;
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
		
	}
	
	
	
}
