package hr.algebra.gamearena.api.filter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class UnconfiguredEndpointDenier implements AuthorizationManager<RequestAuthorizationContext> {

    public static final String UNCONFIGURED_ATTRIBUTE = "UNCONFIGURED_ENDPOINT";

    @Override
    public AuthorizationDecision authorize(@NonNull Supplier<? extends Authentication> authentication, @Nullable RequestAuthorizationContext context) {
        if (context != null) {
            context.getRequest().setAttribute(UNCONFIGURED_ATTRIBUTE, Boolean.TRUE);
        }
        return new AuthorizationDecision(false);
    }
}
