package org.example;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RTest {
    @Resource
    private WebApplicationContext context;
    private String json;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        json = "{\"data\":\"5, 4, 5, 8, 9, 1, 2, 3, 2, 1, 7, 6\"}";
    }

    @Test
    public void requestForRStatistics_ReturnOk() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/statistic")
            .contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(print()).andReturn();
        String response = result.getResponse().getContentAsString();
        Assert.assertTrue(response.contains("data_mean"));
        Assert.assertTrue(response.contains("data_median"));
        Assert.assertTrue(response.contains("data_var"));
        Assert.assertTrue(response.contains("data_sd"));
    }

}
