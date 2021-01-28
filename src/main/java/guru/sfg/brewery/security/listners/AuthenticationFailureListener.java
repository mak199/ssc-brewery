package guru.sfg.brewery.security.listners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listener(AuthenticationFailureBadCredentialsEvent event){
        log.debug("Login Failure");

        LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();

        if(event.getSource() instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            if(token.getPrincipal() instanceof String){
                log.debug("Attempted User:"+token.getPrincipal());
                builder.username((String) token.getPrincipal());
                userRepository.findByUsername((String)token.getPrincipal()).ifPresent(builder::user);
            }


            if(token.getDetails() instanceof WebAuthenticationDetails){
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                log.debug("Source IP:"+details.getRemoteAddress());
                builder.sourceIp(details.getRemoteAddress());
            }

            LoginFailure failure = loginFailureRepository.save(builder.build());
            log.debug("Failure Event:"+failure.getId());
        }
    }
}
