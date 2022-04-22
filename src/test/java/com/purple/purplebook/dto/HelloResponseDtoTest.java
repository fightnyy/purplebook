package com.purple.purplebook.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HelloResponseDtoTest {

    @Test
    void 롬복_기능_테스트 () throws Exception {
      //given
      String name = "test";
      int amount = 1000;
      //when
        HelloResponseDto dto = new HelloResponseDto(name, amount);
        //then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
     }

}