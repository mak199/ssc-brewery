package guru.sfg.brewery.security.perms.BeerOrder;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('order.read', 'customer.order.read')")
public @interface BeerOrderReadPermissionV2 {
}
