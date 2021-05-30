package com.danilat.scorecards.controllers.editor;

import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditorAuthFilter extends OncePerRequestFilter {
    private TokenValidator tokenValidator;
    private AccountRepository accountRepository;

    Logger logger = LoggerFactory.getLogger(EditorAuthFilter.class);

    public EditorAuthFilter(TokenValidator tokenValidator, AccountRepository accountRepository) {
        this.tokenValidator = tokenValidator;
        this.accountRepository = accountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> optionalAccessToken = getOptionalAccessToken(request);
        if (optionalAccessToken.isPresent()) {
            logger.info("There are an access token {}", optionalAccessToken);
            TokenValidator.UserFromToken user = tokenValidator.validateToken(optionalAccessToken.get().getValue());
            if (isAnEditor(user)) {
                logger.info("There current user is an editor");
                filterChain.doFilter(request, response);
                return;
            }
        }
        logger.info("There current user not is an editor");
        response.sendRedirect("/accounts/login");
    }

    private Optional<Cookie> getOptionalAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.info("The cookies are not present in the request");
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("access_token")).findFirst();
    }

    private boolean isAnEditor(TokenValidator.UserFromToken user) {
        AtomicBoolean isAnEditor = new AtomicBoolean(false);
        if (user != null) {
            logger.info("There are an user in the token {}", user);
            accountRepository.findByEmail(user.getEmail()).ifPresent(account -> isAnEditor.set(account.isEditor()));
        }
        return isAnEditor.get();
    }
}
