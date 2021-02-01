package com.thiagothomas.cursomc.services;

import com.thiagothomas.cursomc.domain.Cidade;
import com.thiagothomas.cursomc.domain.Cliente;
import com.thiagothomas.cursomc.domain.Endereco;
import com.thiagothomas.cursomc.domain.enums.TipoCliente;
import com.thiagothomas.cursomc.dto.ClienteDTO;
import com.thiagothomas.cursomc.dto.ClienteNewDTO;
import com.thiagothomas.cursomc.repositories.ClienteRepository;
import com.thiagothomas.cursomc.repositories.EnderecoRepository;
import com.thiagothomas.cursomc.services.exceptions.DataIntegrityException;
import com.thiagothomas.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		 "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return repo.save(obj);	
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque há entidades relacioandas");
		}
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linePerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linePerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}

	public Cliente fromDTO(ClienteNewDTO objDtO) {
		Cliente cli = new Cliente(null,
				objDtO.getNome(),
				objDtO.getEmail(),
				objDtO.getCpfOuCnpj(),
				TipoCliente.toEnum(objDtO.getTipo()));
		Cidade cid =new Cidade(objDtO.getCidadeId(), null, null);
		Endereco end = new Endereco(null,
				objDtO.getLogradouro(),
				objDtO.getNumero(),
				objDtO.getComplemento(),
				objDtO.getBairro(),
				objDtO.getCep(),
				cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDtO.getTelefone1());

		if(objDtO.getTelefone2() != null) {
			cli.getTelefones().add(objDtO.getTelefone2());
		}
		if(objDtO.getTelefone3() != null) {
			cli.getTelefones().add(objDtO.getTelefone3());
		}
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
