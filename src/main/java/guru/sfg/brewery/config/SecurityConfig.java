package guru.sfg.brewery.config;
import guru.sfg.brewery.security.JPAUserDetailService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // constructs restHeaderAuthFilter
    // configuration of filter done here
    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager){
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    // allowing web traffic to go to those specific URLs
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // telling spring security to add filter in filter chain before the usernamePasswordAuthenticationFilter
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();


        http.authorizeRequests(authorize->{
            authorize
                    .antMatchers("/h2-console/**").permitAll() // do not use in production
                    .antMatchers("/","/beers/find","/webjars/**","/resources/**").permitAll()
                    .mvcMatchers(HttpMethod.GET,"/api/v1/beer/**").permitAll()
                    .mvcMatchers(HttpMethod.DELETE,"/api/v1/beer/**").hasRole("ADMIN")
                    .mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll()
                    .mvcMatchers("/brewery/breweries")
                        .hasAnyRole("ADMIN","CUSTOMER")
                    .mvcMatchers(HttpMethod.GET,"/brewery/api/v1/breweries")
                        .hasAnyRole("ADMIN","CUSTOMER");
        }).authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

        // h2 console config, allows h2-console to function normally by allowing h2 frames
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // {noop} stores password as free text and does nto encrypt it
    // different users and different password encoders
    // the key in front of encoders tells spring which encoding is worming for the password
    //@Override
   // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(this.jpaUserDetailService).passwordEncoder(passwordEncoder());

        /*auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{bcrypt}$2a$10$ZjguNDOfg/GNnDuQsommReZHcrjc0E/oYeKefGrWzQtFpBGijpLli")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}cd3daa627b56f9b74fe89837ca85b1f9a0574190b21ee4e0cd989a91f5333d0ef9beffaae5e70350")
                .roles("USER")
        ;
        auth.inMemoryAuthentication().withUser("scott").password("{bcrypt15}$2a$15$F5ReVlRyjiLIGL6QX5E7puVvsI3dFUKVH5huR5GDILYkTc/EcYd/K").roles("CUSTOMER");
        */
   // }

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
