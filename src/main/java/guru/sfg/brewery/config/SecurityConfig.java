package guru.sfg.brewery.config;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    // allowing web traffic to go to those specific URLs
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize->{
            authorize
                    .antMatchers("/","/beers/find","/webjars/**","/resources/**").permitAll()
                    .mvcMatchers(HttpMethod.GET,"/api/v1/beer/**").permitAll()
                    .mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll();
        }).authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

    // {noop} stores password as free text and does nto encrypt it
    // different users and different password encoders
    // the key in front of encoders tells spring which encoding is worming for the password
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{bcrypt}$2a$10$ZjguNDOfg/GNnDuQsommReZHcrjc0E/oYeKefGrWzQtFpBGijpLli")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}cd3daa627b56f9b74fe89837ca85b1f9a0574190b21ee4e0cd989a91f5333d0ef9beffaae5e70350")
                .roles("USER")
        ;
        auth.inMemoryAuthentication().withUser("scott").password("{ldap}{SSHA}VbkhtZegdcypR2f6CexzsCIWckIvINR6xQ2f9Q==").roles("CUSTOMER");
    }

    // this overrides the spring security auto configuration implementation in properties file
  /*  @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("spring")
                .password("guru")
                .roles("ADMIN")
                .build();
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin,user);
     }*/
}
