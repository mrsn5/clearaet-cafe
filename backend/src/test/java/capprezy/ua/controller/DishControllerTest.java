package capprezy.ua.controller;


import capprezy.ua.service.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static capprezy.ua._helper.PageableAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
public class DishControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @MockBean
    private DishService dishService;


    @WithMockUser("admin@gmail.com")
    @Test
    public void evaluatesPageableParameter() throws Exception {
        mvc.perform(get("/api/dish")
                .param("page", "5")
                .param("size", "10")
                .param("sort", "uid,desc")   // <-- no space after comma!
                .param("sort", "name,asc")) // <-- no space after comma!
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);
        verify(dishService).findByCriteria(
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertThat(pageable).hasPageNumber(5);
        assertThat(pageable).hasPageSize(10);
        assertThat(pageable).hasSort("name", Sort.Direction.ASC);
        assertThat(pageable).hasSort("uid", Sort.Direction.DESC);
    }
}
