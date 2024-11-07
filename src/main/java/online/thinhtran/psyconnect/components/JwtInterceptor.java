package online.thinhtran.psyconnect.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.services.UserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtInterceptor implements ChannelInterceptor {
    private final JwtGenerator jwtGenerator;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("preSend triggered");

        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);


        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String jwtToken = authorizationHeader.substring(7);

            String username = jwtGenerator.extractUsername(jwtToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(userDetails);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                accessor.setUser(authenticationToken);
                Objects.requireNonNull(accessor.getSessionAttributes()).put("simpUser", authenticationToken.getPrincipal());
                log.info("Header: {}", accessor);
            }
        }

        return message;
    }
}
