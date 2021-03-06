package com.tiago.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tiago.cursomc.domain.Cidade;
import com.tiago.cursomc.domain.Cliente;
import com.tiago.cursomc.domain.Endereco;
import com.tiago.cursomc.domain.enums.Perfil;
import com.tiago.cursomc.domain.enums.TipoCliente;
import com.tiago.cursomc.dto.ClienteDTO;
import com.tiago.cursomc.dto.ClienteNewDTO;
import com.tiago.cursomc.repositories.ClienteRepository;
import com.tiago.cursomc.repositories.EnderecoRepository;
import com.tiago.cursomc.security.UserSS;
import com.tiago.cursomc.services.exceptions.AuthorizationException;
import com.tiago.cursomc.services.exceptions.DataIntegrityException;
import com.tiago.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	@Autowired
	private BCryptPasswordEncoder enc;

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository repoEnd;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imgService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;

	public Cliente findById(Integer id) {
		
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Object not found! ID: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		repoEnd.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = findById(obj.getId());
		updateDate(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		findById(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Not possible to delete 'cause there entities related");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getName(), objDto.getEmail(), null, null,null);
	}
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli =  new Cliente(null,objDto.getName(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()),enc.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(),null,null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone());
		if(objDto.getTelefone1()!=null) {
			cli.getTelefones().add(objDto.getTelefone1());
		}
		if(objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		return cli;
	}

	private void updateDate(Cliente newObj, Cliente obj) {
		newObj.setName(obj.getName());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imgService.getJpgImageFromFile(multipartFile);
		jpgImage = imgService.cropSquare(jpgImage);
		jpgImage = imgService.resize(jpgImage, size);
		
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imgService.getInputStream(jpgImage, "jpg"), fileName, "image");
		
		
	}

}
