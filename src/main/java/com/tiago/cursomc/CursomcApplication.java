package com.tiago.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tiago.cursomc.domain.Categoria;
import com.tiago.cursomc.domain.Cidade;
import com.tiago.cursomc.domain.Cliente;
import com.tiago.cursomc.domain.Endereco;
import com.tiago.cursomc.domain.Estado;
import com.tiago.cursomc.domain.Product;
import com.tiago.cursomc.domain.enums.TipoCliente;
import com.tiago.cursomc.repositories.CategoriaRepository;
import com.tiago.cursomc.repositories.CidadeRepository;
import com.tiago.cursomc.repositories.ClienteRepository;
import com.tiago.cursomc.repositories.EnderecoRepository;
import com.tiago.cursomc.repositories.EstadoRepository;
import com.tiago.cursomc.repositories.ProductRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriaRepository categoriaRepo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private CidadeRepository cidadeRepo;
	@Autowired
	private EstadoRepository estadoRepo;
	@Autowired
	private ClienteRepository cliRepo;
	@Autowired
	private EnderecoRepository endeRepo;
	

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");
		
		Product p1 = new Product(null, "computador", 2000.00);
		Product p2 = new Product(null, "impressora", 800.00);
		Product p3 = new Product(null, "mouse", 80.00);	
		
		cat1.getProducts().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProducts().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlandia",est1);
		Cidade c2 = new Cidade(null, "São Paulo",est2);
		Cidade c3 = new Cidade(null, "Campinas",est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		
		categoriaRepo.saveAll(Arrays.asList(cat1,cat2));
		productRepo.saveAll(Arrays.asList(p1,p2,p3));
		estadoRepo.saveAll(Arrays.asList(est1,est2));
		cidadeRepo.saveAll(Arrays.asList(c1,c2,c3));
		
		Cliente cli1 = new Cliente(null, "Maria", "maria@gmail.com", "36378912345", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("45678945","123465789"));
		
		Endereco e1 = new Endereco(null, "rua flores", "123", "casa", "jardim", "123456-78", cli1, c1);
		Endereco e2 = new Endereco(null, "Av maros", "103", "sala 300", "centro", "78945-78", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		cliRepo.saveAll(Arrays.asList(cli1));
		endeRepo.saveAll(Arrays.asList(e1,e2));
		
		
		
		
		
	}

}
