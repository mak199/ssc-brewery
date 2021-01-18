package guru.sfg.brewery.security.perms.Customer;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('customer.create')")
public @interface CustomerCreatePermission {
}
