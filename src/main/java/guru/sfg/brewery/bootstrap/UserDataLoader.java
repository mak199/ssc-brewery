package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataLoader(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Authority customer = authorityRepository.save(Authority.builder().role("ROLE_CUSTOMER").build());
        Authority admin = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
        Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());

        userRepository.save(User.builder().username("spring").password(passwordEncoder.encode("guru")).authority(admin).build());
        userRepository.save(User.builder().username("user").password(passwordEncoder.encode("password")).authority(userRole).build());
        userRepository.save(User.builder().username("scott").password(passwordEncoder.encode("tiger")).authority(customer).build());

        System.out.println("User Loaded:"+userRepository.count());

    }
}
