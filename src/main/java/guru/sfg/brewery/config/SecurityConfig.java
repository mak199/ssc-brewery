package guru.sfg.brewery.config;
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
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new LdapShaPasswordEncoder();
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
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{SSHA}kKcJJMzTKv75jYosoEpuNXm0cI2RV/iIC8ShcA==")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{SSHA}kKcJJMzTKv75jYosoEpuNXm0cI2RV/iIC8ShcA==")
                .roles("USER")
        ;
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
