package guru.sfg.brewery.security.listners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationSuccessListeners {

    @EventListener
    public void listener(AuthenticationSuccessEvent event){
        log.debug("User Logged In Okay");
    }
}
