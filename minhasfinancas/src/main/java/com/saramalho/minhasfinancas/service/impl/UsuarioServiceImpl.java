package com.saramalho.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saramalho.minhasfinancas.exception.ErroAutenticacao;
import com.saramalho.minhasfinancas.exception.RegraNegocioException;
import com.saramalho.minhasfinancas.model.entity.Usuario;
import com.saramalho.minhasfinancas.model.repository.UsuarioRepository;
import com.saramalho.minhasfinancas.service.UsuarioService;




@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;
	
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
	    Optional<Usuario> usuario = repository.findByEmail(email);
		
	    if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario não encontrado.");
		
		}
	    
	    if(!usuario.get().getSenha().equals(senha)){
	    	
	    	throw new ErroAutenticacao("Senha Inválida.");
	    	
	    }
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		
		validarEmail(usuario.getEmail());
		
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Email já cadastrado com um usuário");
		}
	}

	@Override
	public Optional <Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

}

