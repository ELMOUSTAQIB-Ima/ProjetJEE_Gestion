package ENSA.IO.sec;

import ENSA.IO.Entities.AppUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {/*  cette methode elle s'execute au moment ou l'utilisateur tente de s'authentifier c'esta dire quand l'utilisateur
                                                                                                                                              saisie le mot passe et le password et dans cette methode la on va utiliser l'object request pour recuperer
                                                                                                                                              username et le mot de passe parceque on la envoyer en format json ou bien en format 3wurlencoded en fonctionnne
                                                                                                                                              des cas alors si je recupere le username et le mot de passe je vais retourner un object Authentication qui contient
                                                                                                                                              le username et le mot de passe a spring securite.
                                                                                                                                            */
        try {





            AppUser appUser= new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appUser.getUsername()
                            ,appUser.getPassword()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request
            , HttpServletResponse response,
            FilterChain chain
            , Authentication authResult
    ) throws IOException, ServletException {



        User user=(User)authResult.getPrincipal();
        List<String> roles=new ArrayList<>();

        authResult.getAuthorities().forEach(a->{
            roles.add(a.getAuthority());
        });



        String jwt= JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(user.getUsername())
                .withArrayClaim("roles",roles.toArray(new String[roles.size()]))
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION))
                .sign(Algorithm.HMAC256(SecurityParams.SECRET));
        response.addHeader(SecurityParams.JWT_HEADER_NAME
                ,SecurityParams.HEADER_PREFIX+jwt);

    }

}
