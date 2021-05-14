package com.danilat.scorecards.controllers.editor;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.danilat.scorecards.controllers.BaseControllerTest;
import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.AccountMother;
import com.danilat.scorecards.core.mothers.BoxerMother;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;

public class EditorBoxersControllerTest extends BaseControllerTest {

    @Autowired
    private BoxerRepository boxerRepository;
    @Autowired
    private AccountRepository accountRepository;
    private Account account = AccountMother.anEditorAccountWithUsername("an_username");
    private Cookie cookie;

    @Before
    public void setup() {
        boxerRepository.clear();
        accountRepository.clear();
        accountRepository.save(account);
        cookie = getCookieFor(account);
    }

    @Test
    public void getTheBoxersList() throws Exception {
        boxerRepository.save(BoxerMother.aBoxerWithId("irrelevant-id"));

        this.mvc.perform(get("/editor/boxers").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(model().attribute("boxers", notNullValue()))
                .andExpect(model().attribute("boxers", hasSize(1)));
    }

    @Test
    public void getTheBoxersDetail() throws Exception {
        boxerRepository.save(BoxerMother.aBoxerWithId("ali"));

        this.mvc.perform(get("/editor/boxers/ali").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(model().attribute("boxer", notNullValue()));
    }

    @Test
    public void getTheBoxersDetailWhenNotExist() throws Exception {
        this.mvc.perform(get("/editor/boxers/foobar").cookie(cookie))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getsTheFormToCreateABoxer() throws Exception {
        this.mvc.perform(get("/editor/boxers/new").cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    public void postANewBoxerWithValidParameters() throws Exception {
        this.mvc.perform(post("/editor/boxers").cookie(cookie)
                .param("name", "Manny Paqciao")
                .param("alias", "pacman")
                .param("boxrecUrl", "https://boxrec.com/en/proboxer/6129"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/editor/boxers"));
    }

    @Test
    public void postANewBoxerWithInValidParameters() throws Exception {
        this.mvc.perform(post("/editor/boxers").cookie(cookie)
                .param("name", "")
                .param("alias", "pacman")
                .param("boxrecUrl", "https://boxrec.com/en/proboxer/6129"))
                .andExpect(status().isOk())
                .andExpect(view().name("editor/boxers/new"));
    }

    @Test
    public void updateABoxerWithValidParameters() throws Exception {
        Boxer boxer = BoxerMother.aBoxerWithId("ali");
        boxerRepository.save(BoxerMother.aBoxerWithId("ali"));

        this.mvc.perform(post("/editor/boxers/ali").cookie(cookie)
                .param("name", boxer.name())
                .param("alias", "The Greatest!")
                .param("boxrecUrl", boxer.boxrecUrl()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/editor/boxers"));
    }

    @Test
    public void updateABoxerWithInvalidParameters() throws Exception {
        Boxer boxer = BoxerMother.aBoxerWithId("ali");
        boxerRepository.save(BoxerMother.aBoxerWithId("ali"));

        this.mvc.perform(post("/editor/boxers/ali").cookie(cookie)
                .param("name", "")
                .param("alias", boxer.alias())
                .param("boxrecUrl", boxer.boxrecUrl()))
                .andExpect(status().isOk())
                .andExpect(view().name("editor/boxers/edit"));
    }
}
