package com.tiago.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiago.cursomc.domain.Cliente;
import com.tiago.cursomc.domain.ItemPedido;
import com.tiago.cursomc.domain.PagamentoComBoleto;
import com.tiago.cursomc.domain.Pedido;
import com.tiago.cursomc.domain.enums.EstadoPagamento;
import com.tiago.cursomc.repositories.ClienteRepository;
import com.tiago.cursomc.repositories.ItemPedidoRepository;
import com.tiago.cursomc.repositories.PagamentoRepository;
import com.tiago.cursomc.repositories.PedidoRepository;
import com.tiago.cursomc.security.UserSS;
import com.tiago.cursomc.services.exceptions.AuthorizationException;
import com.tiago.cursomc.services.exceptions.ObjectNotFoundException;



@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	@Autowired
	private BoletoService boletoService;
	@Autowired
	private PagamentoRepository pagtorepo;
	@Autowired
	private ProdutoService proServ;	
	@Autowired 
	private ItemPedidoRepository itemrepo;
	@Autowired
	private ClienteService clieServ;
	@Autowired
	private EmailService emailServ;
	
	public Pedido findById(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Object not found! ID: "+ id +", Tipo: "+ Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clieServ.findById(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		pedido = repo.save(pedido);
		pagtorepo.save(pedido.getPagamento());
		for(ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(proServ.findById(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPrice());
			ip.setPedido(pedido);
		}
		itemrepo.saveAll(pedido.getItens());
		emailServ.sendOrderConfirmationHtmlEmail(pedido);
		return pedido;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage,  String direction, String orderBy){
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clieServ.findById(user.getId());
		
		return repo.findByCliente(cliente, pageRequest);
	}
}
