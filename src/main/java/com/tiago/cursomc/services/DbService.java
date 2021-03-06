package com.tiago.cursomc.services;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tiago.cursomc.domain.Categoria;
import com.tiago.cursomc.domain.Cidade;
import com.tiago.cursomc.domain.Cliente;
import com.tiago.cursomc.domain.Endereco;
import com.tiago.cursomc.domain.Estado;
import com.tiago.cursomc.domain.ItemPedido;
import com.tiago.cursomc.domain.Pagamento;
import com.tiago.cursomc.domain.PagamentoComBoleto;
import com.tiago.cursomc.domain.PagamentoComCartao;
import com.tiago.cursomc.domain.Pedido;
import com.tiago.cursomc.domain.Produto;
import com.tiago.cursomc.domain.enums.EstadoPagamento;
import com.tiago.cursomc.domain.enums.Perfil;
import com.tiago.cursomc.domain.enums.TipoCliente;
import com.tiago.cursomc.repositories.CategoriaRepository;
import com.tiago.cursomc.repositories.CidadeRepository;
import com.tiago.cursomc.repositories.ClienteRepository;
import com.tiago.cursomc.repositories.EnderecoRepository;
import com.tiago.cursomc.repositories.EstadoRepository;
import com.tiago.cursomc.repositories.ItemPedidoRepository;
import com.tiago.cursomc.repositories.PagamentoRepository;
import com.tiago.cursomc.repositories.PedidoRepository;
import com.tiago.cursomc.repositories.ProdutoRepository;

@Service
public class DbService {
	
	
	@Autowired
	private CategoriaRepository categoriaRepo;
	@Autowired
	private ProdutoRepository productRepo;
	@Autowired
	private CidadeRepository cidadeRepo;
	@Autowired
	private EstadoRepository estadoRepo;
	@Autowired
	private ClienteRepository cliRepo;
	@Autowired
	private EnderecoRepository endeRepo;
	@Autowired
	private PedidoRepository pedRepo;
	@Autowired
	private PagamentoRepository pagRepo;
	@Autowired
	private ItemPedidoRepository itemPedRepo;
	@Autowired
	private BCryptPasswordEncoder enc;
	
	public void instantiateTestDatabase() throws ParseException {
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");
		Categoria cat3 = new Categoria(null, "Cama mesa e banho");
		Categoria cat4 = new Categoria(null, "Eletronicos");
		Categoria cat5 = new Categoria(null, "Jardinagem");
		Categoria cat6 = new Categoria(null, "Decora????o");
		Categoria cat7 = new Categoria(null, "Perfumaria");
		
		Produto p1 = new Produto(null, "computador", 2000.00);
		Produto p2 = new Produto(null, "impressora", 800.00);
		Produto p3 = new Produto(null, "mouse", 80.00);	
		Produto p4 = new Produto(null, "Mesa de escritorio", 300.00);	
		Produto p5 = new Produto(null, "Toalha", 50.00);	
		Produto p6 = new Produto(null, "Colcha", 200.00);	
		Produto p7 = new Produto(null, "TV true color", 1200.00);	
		Produto p8 = new Produto(null, "Ro??adeira", 800.00);	
		Produto p9 = new Produto(null, "Abajur", 100.00);
		Produto p10 = new Produto(null, "Pendente", 180.00);	
		Produto p11 = new Produto(null, "Shampoo", 90.00);	
		
		
		cat1.getProducts().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProducts().addAll(Arrays.asList(p2,p4));
		cat3.getProducts().addAll(Arrays.asList(p5,p6));
		cat4.getProducts().addAll(Arrays.asList(p1,p2,p3,p7));
		cat5.getProducts().addAll(Arrays.asList(p8));
		cat6.getProducts().addAll(Arrays.asList(p9,p10));
		cat7.getProducts().addAll(Arrays.asList(p11));
		

		p1.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2, cat4));
		p3.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p4.getCategorias().addAll(Arrays.asList(cat2));
		p5.getCategorias().addAll(Arrays.asList(cat3));
		p6.getCategorias().addAll(Arrays.asList(cat3));
		p7.getCategorias().addAll(Arrays.asList(cat4));
		p8.getCategorias().addAll(Arrays.asList(cat5));
		p9.getCategorias().addAll(Arrays.asList(cat6));
		p10.getCategorias().addAll(Arrays.asList(cat6));
		p11.getCategorias().addAll(Arrays.asList(cat7));		

		categoriaRepo.saveAll(Arrays.asList(cat1,cat2,cat3,cat4,cat5,cat6,cat7));
		productRepo.saveAll(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "S??o Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlandia",est1);
		Cidade c2 = new Cidade(null, "S??o Paulo",est2);
		Cidade c3 = new Cidade(null, "Campinas",est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		
		
		estadoRepo.saveAll(Arrays.asList(est1,est2));
		cidadeRepo.saveAll(Arrays.asList(c1,c2,c3));
		
		Cliente cli1 = new Cliente(null, "Maria", "maria@gmail.com", "36378912345", TipoCliente.PESSOAFISICA,enc.encode("1234"));
		cli1.getTelefones().addAll(Arrays.asList("45678945","123465789"));
		
		Cliente cli2 = new Cliente(null, "Tiago", "tiagohs.199@gmail.com", "62194162364", TipoCliente.PESSOAFISICA,enc.encode("1234"));
		cli2.addPerfil(Perfil.ADMIN);
		cli2.getTelefones().addAll(Arrays.asList("45678945","123465789"));
		
		
		Endereco e1 = new Endereco(null, "rua flores", "123", "casa", "jardim", "123456-78", cli1, c1);
		Endereco e2 = new Endereco(null, "Av maros", "103", "sala 300", "centro", "78945-78", cli1, c2);
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		Endereco e3 = new Endereco(null, "Av joao", "13",null, "centro", "78945-78", cli2, c3);
		cli2.getEnderecos().addAll(Arrays.asList(e3));
		
		
		cliRepo.saveAll(Arrays.asList(cli1,cli2));
		endeRepo.saveAll(Arrays.asList(e1,e2,e3));
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		Pagamento pag1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pag1);
		
		Pagamento pag2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pag2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		pedRepo.saveAll(Arrays.asList(ped1,ped2));
		
		pagRepo.saveAll(Arrays.asList(pag1,pag2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip2));
		p3.getItens().addAll(Arrays.asList(ip3));
		
		
		itemPedRepo.saveAll(Arrays.asList(ip1,ip2,ip3));
	}

}
