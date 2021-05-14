package com.danilat.scorecards;

import com.danilat.scorecards.controllers.editor.EditorAuthFilter;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ScorecardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScorecardsApplication.class, args);
    }

    @Autowired
    private TokenValidator tokenValidator;
    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public FilterRegistrationBean<EditorAuthFilter> editorAuthFilter() {
        FilterRegistrationBean<EditorAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EditorAuthFilter(tokenValidator, accountRepository));
        registrationBean.addUrlPatterns("/editor", "/editor/*");
        return registrationBean;
    }
}
