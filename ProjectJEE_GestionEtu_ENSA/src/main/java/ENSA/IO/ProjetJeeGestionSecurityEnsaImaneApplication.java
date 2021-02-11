package ENSA.IO;

import ENSA.IO.Entities.AppRole;
import ENSA.IO.Entities.AppUser;
import ENSA.IO.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class ProjetJeeGestionSecurityEnsaImaneApplication {

    @Autowired
    private RepositoryRestConfiguration repositoryRestConfiguration;


    public static void main(String[] args) {
        SpringApplication.run(ProjetJeeGestionSecurityEnsaImaneApplication.class, args);
    }
    @Bean
    CommandLineRunner start(AccountService accountService){
        return args->{

            repositoryRestConfiguration.exposeIdsFor(AppUser.class, AppRole.class);
            accountService.save(new AppRole(null,"USER"));
            accountService.save(new AppRole(null,"ADMIN"));
            Stream.of("user1","user2","user3","imane").forEach(un->{
                accountService.saveUser(un,"1234","1234");
            });
            accountService.addRoleToUser("imane","ADMIN");
        };
    }
    @Bean
    BCryptPasswordEncoder getBCPE(){
        return new BCryptPasswordEncoder();

    }

}
