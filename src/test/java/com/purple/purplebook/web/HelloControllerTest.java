package com.purple.purplebook.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.is;

import com.purple.purplebook.dto.HelloResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void hello가_리턴된다() throws Exception {
        //given
        String hello = "hello";

        //when
        mvc.perform(get("/hello"))
           //then
           .andExpect(status().isOk())
           .andExpect(content().string(hello));
    }

    @Test
    void helloDto가_리턴된다() throws Exception {
        //given
        String name = "hello";
        int amount = 1000;
        HelloResponseDto dto = new HelloResponseDto(name, amount);
        //when
        mvc.perform(get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
        //then
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is(name)))
           .andExpect(jsonPath("$.amount", is(amount)));

    }

}