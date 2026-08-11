package hr.algebra.gamearena.api.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;

import java.util.function.Supplier;

@Component
public class UnconfiguredEndpointDenier implements AuthorizationManager<RequestAuthorizationContext> {

    public static final String UNCONFIGURED_ATTRIBUTE = "UNCONFIGURED_ENDPOINT";

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public UnconfiguredEndpointDenier(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public AuthorizationDecision authorize(
            @NonNull Supplier<? extends Authentication> authentication,
            @Nullable RequestAuthorizationContext context
    ) {
        if (context != null && declaresPreAuthorize(context.getRequest())) {
            return null;
        }

        if (context != null) {
            context.getRequest().setAttribute(UNCONFIGURED_ATTRIBUTE, Boolean.TRUE);
        }
        return new AuthorizationDecision(false);
    }

    private boolean declaresPreAuthorize(
            HttpServletRequest request
    ) {
        try {
            if (!ServletRequestPathUtils.hasParsedRequestPath(request)) {
                ServletRequestPathUtils.parseAndCache(request);
            }

            HandlerExecutionChain chain = requestMappingHandlerMapping.getHandler(request);
            if (chain == null || !(chain.getHandler() instanceof HandlerMethod handlerMethod)) {
                return false;
            }

            return AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), PreAuthorize.class)
                    || AnnotatedElementUtils.hasAnnotation(handlerMethod.getBeanType(), PreAuthorize.class);
        } catch (Exception e) {
            return false;
        }
    }
}
