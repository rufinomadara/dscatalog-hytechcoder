package com.hytechcoder.dscatalog.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hytechcoder.dscatalog.dto.ProductDTO;
import com.hytechcoder.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/products") 
public class ProductResource {
	
	@Autowired
	private ProductService service;
	
//	método que lista todas as categorias, chama a service 
//	@GetMapping
//	public ResponseEntity<List<ProductDTO>> finAll(){
//		List<ProductDTO> list = service.findAll();
//		
//		return ResponseEntity.ok().body(list);
//	}
	
//	@GetMapping
//	public ResponseEntity<Page<ProductDTO>> finAll(
//			@RequestParam(value = "page", defaultValue = "0") Integer page,
//			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
//			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
//			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy
//			){
//		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
//		
//		Page<ProductDTO> list = service.findAllPaged(pageRequest);
//		return ResponseEntity.ok().body(list);
//	}
//	
////	Método que retorna a busca de uma categoria
//	@GetMapping(value = "/{id}")
//	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
//
//		ProductDTO dto = service.findById(id);
//
//		return ResponseEntity.ok().body(dto);
//	}
	
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> finAll(Pageable pageable){
		Page<ProductDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}
	
//	Método que retorna a busca de uma categoria
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){

		ProductDTO dto = service.findById(id);

		return ResponseEntity.ok().body(dto);
	}
	
//	Médodo que inseri uma nova categoria retornando status 201 se for criada com sucesso
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping (value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build(); //Dá uma resposta 204 deu certo e o corpo da resposta está vázio
	}
	
	
}
