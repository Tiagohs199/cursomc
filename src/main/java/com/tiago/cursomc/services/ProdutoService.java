package com.tiago.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.tiago.cursomc.domain.Categoria;
import com.tiago.cursomc.domain.Produto;
import com.tiago.cursomc.repositories.CategoriaRepository;
import com.tiago.cursomc.repositories.ProdutoRepository;
import com.tiago.cursomc.services.exceptions.ObjectNotFoundException;



@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository catRepo;

	public Produto findById(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Object not found! ID: "+ id +", Tipo: "+ Produto.class.getName()));
	}
	
	public Page<Produto> search(String name,List<Integer> ids,Integer page, Integer linesPerPage, String orderBy,  String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = catRepo.findAllById(ids);
		
		return repo.findDistinctByNameContainingAndCategoriasIn(name,categorias, pageRequest);
	}
	
	
	
}
