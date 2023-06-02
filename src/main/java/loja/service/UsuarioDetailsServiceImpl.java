package loja.service;

import loja.model.Perfil;
import loja.model.Usuario;
import loja.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(getAuthorities(usuario.getPerfis()))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Perfil> perfis) {
        return perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.getNome()))
                .collect(Collectors.toList());
    }
}
