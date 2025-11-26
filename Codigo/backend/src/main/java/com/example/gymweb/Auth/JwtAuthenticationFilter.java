package com.example.gymweb.Auth;

<<<<<<< Updated upstream
import com.example.gymweb.model.Usuario;
import com.example.gymweb.repository.UsuarioRepository;
=======
import com.example.gymweb.Repository.UsuarioRepository;
import com.example.gymweb.model.Usuario;

>>>>>>> Stashed changes
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
<<<<<<< Updated upstream
=======
import java.io.IOException;
import java.util.List;
>>>>>>> Stashed changes
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

<<<<<<< Updated upstream
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

=======
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
>>>>>>> Stashed changes
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

<<<<<<< Updated upstream
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String email = jwtService.extraerSubject(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);

            if (usuario != null && jwtService.esTokenValido(token)) {

                // Convertimos el rol a GrantedAuthority
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                usuario,
                                null,
                                List.of(authority) // <-- IMPORTANTE!!
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
=======
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = this.jwtService.extraerSubject(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Usuario usuario = (Usuario)this.usuarioRepository.findByEmailIgnoreCase(email).orElse((Usuario) null);
                if (usuario != null && this.jwtService.esTokenValido(token)) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(usuario, (Object)null, List.of(authority));
                    authToken.setDetails((new WebAuthenticationDetailsSource()).buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
>>>>>>> Stashed changes
    }
}
