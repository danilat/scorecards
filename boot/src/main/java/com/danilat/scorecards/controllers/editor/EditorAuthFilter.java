package com.danilat.scorecards.controllers.editor;

import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
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

    public EditorAuthFilter(TokenValidator tokenValidator, AccountRepository accountRepository) {
        this.tokenValidator = tokenValidator;
        this.accountRepository = accountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> optionalAccessToken = getOptionalAccessToken(request);
        if (optionalAccessToken.isPresent()) {
            TokenValidator.Token token = tokenValidator.validateToken(optionalAccessToken.get().getValue());
            if (isAnEditor(token)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.sendRedirect("/accounts/login");
    }

    private Optional<Cookie> getOptionalAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("access_token")).findFirst();
    }

    private boolean isAnEditor(TokenValidator.Token token) {
        AtomicBoolean isAnEditor = new AtomicBoolean(false);
        if (token != null) {
            accountRepository.findByEmail(token.getEmail()).ifPresent(account -> isAnEditor.set(account.isEditor()));
        }
        return isAnEditor.get();
    }
}
