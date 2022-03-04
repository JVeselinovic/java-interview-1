package io.delfidx.helpers;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.security.token.TokenAuthenticationFetcher;
import io.micronaut.security.token.reader.TokenResolver;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Requires(env = Environment.TEST)
@Replaces(TokenAuthenticationFetcher.class)
@Singleton
public class TokenMock implements AuthenticationFetcher {
    public static final Logger log = LoggerFactory.getLogger(TokenMock.class);
    private final TokenResolver tokenResolver;
    private List<String> overrideRoles;

    @Inject
    public TokenMock(TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
        this.overrideRoles = Collections.emptyList();
    }

    public List<String> getOverrideRoles() {
        return overrideRoles;
    }

    public void setOverrideRoles(List<String> overrideRoles) {
        this.overrideRoles = overrideRoles;
    }

    public void reset(){
        overrideRoles = Collections.emptyList();
    }

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        Optional<String> token = tokenResolver.resolveToken(request);

        if (token.isEmpty()) {
            return Flux.empty();
        }
        String tokenValue = token.get();
        try {
            SignedJWT jwt = (SignedJWT)JWTParser.parse(tokenValue);
            Payload payload = jwt.getPayload();
            Map<String, Object> jsonObject = payload.toJSONObject();
            String sub = (String) jsonObject.get("sub");
            List tokenRoles = (List)jsonObject.get("permissions");
            List roles = overrideRoles.isEmpty() ? tokenRoles : overrideRoles;
            log.info(String.format("Using mock authentication user: %s, roles: %s", sub, roles));
            return Flux.just(new ClientAuthentication(sub, Map.of("roles", roles)));
        } catch (ParseException e) {
            log.error(e.getMessage());
            return Flux.empty();
        }
    }
}
