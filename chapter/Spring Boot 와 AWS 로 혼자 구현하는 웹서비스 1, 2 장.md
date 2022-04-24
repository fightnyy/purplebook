# Spring Boot와 AWS 로 혼자 구현하는 웹서비스 1, 2 장



> 이동욱님이 쓰신 "스프링부트와  AWS로 혼자 구현하는 웹 서비스" 를 현재(SpringBoot 2.6.7) 버전에 맞게 일부 변형했습니다. 전반적인 내용은 동일합니다.

## 1장

intellij 의 간단한 세팅 및 github 와 intellij 를 연결한 부분이 나옵니다. 이 부분은 다른 블로그 글도 있으니 참고하셔도 될 거 같습니다.



## 2장

테스트 코드는 현재 웹 서비스에서 매우 중요한 요소입니다. 테스트 코드에서 꼭 짚고 넘어가야 하는것은 TDD와 단위테스트(unit test)입니다.



### 2.1 테스트 코드 소개

#### TDD

테스트가 주도하는 개발(Test-Driven-Development, 혹은 Test-First-Development) 을 의미합니다.

![img](https://t1.daumcdn.net/cfile/tistory/99988A3F5AD9C09E39)

* 항상 실패하는 테스트를 먼저 작성하고(RED)
* 테스트가 통과하는 프로덕션 코드를 작성하고(Green)
* 테스트가 통과하면 프로덕션 코드를 리팩토링합니다.(Refacetor)

더 자세한 내용은 (https://repo.yona.io/doortts/blog/issue/1) 를 참고하면 됩니다.



#### 단위 테스트

TDD 의 첫번째 단계인 **기능 단위의 테스트 코드를 작성** 을 얘기합니다. 즉, 순수하게 테스트 코드만 작성하는 것을 얘기합니다. 



테스트 코드를 작성할 때 이점은 무엇일까요? 불확실성을 많이 제거 해준다, 나중에 개발자가 코드를 리팩토링 하거나 라이브러리를 업그레이드 등에 기존 기능이 올바르게 작동하는지를 확인할 수 있습니다.(예, 회귀 테스트) 등 여러가지가 있지만 가장 크게 공감할 수 있는건 톰캣을 띄워서 `System.out.println` 등으로 **확인할 시간을 줄여준다.** 는 측면이 있습니다. 톰캣을 띄우면 크게 몇 분 이상이 소요됩니다. 만약 테스트가 개발자가 원하는대로 나오지 않으면 다시 톰캣을 재시작해야 합니다. 이 과정을 계속하면 크게 몇 시간이 소비되기도 합니다.

또한 **자동검증** 이 되게 합니다. 작성된 단위테스트를 실행만 하면 더는 수동검증은 필요가 없습니다.

마지막으로, **개발자가 만든 기능을 안전하게 보호** 해줍니다. 코드를 작성하다보면 여러 코드들이 상호간 영향을 주고 받게 됩니다. 이 과정에서 A 코드를 수정했을 때 B 코드에 영향을 줘 B 코드에 대한 테스트 케이스가 실패해 기존 코드에 영향이 없도록 수정해 줄 수있습니다.



언어별로 테스트 코드 여러 테스트코드 프레임워크가 있습니다. 가장 대중적인 테스트 프레임워크로는 **xUnit** 이 있습니다.

개발환경(x) 에 따라 Unit. 테스트를 도와주는 도구라고 생각하면 됩니다.

* Java - JUnit
* DB - DBUnit
* .net - Unit



본격적으로 코드를 작성해보겠습니다.

```java
package com.purple.purplebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PurpleBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurpleBookApplication.class, args);
    }

}

```



`@SpringBootApplication` 

* 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정됩니다.
* @SpringBootApplication 이 있는 위치부터 설정을 읽어가기 때문에 이 클래스는 **항상 프로젝트의 최상단에 위치** 해야합니다.
* `main` 메소드에서 실행하는 `SpringApplication.run` 으로 인해 내장 WAS(Web Application Server) 를 실행합니다.
  * 내장 WAS란 별도의 외부에 WAS 를 두지 않고 애플리케이션을 실행할 때 내부에서 WAS를 실행하는 것을 의미합니다.
* 스프링 부트에선 내장 WAS 사용을 권장합니다.
  * 언제 어디서나 같은 환경에서 스프링 부트를 배포할 수 있기 때문입니다.

[이전 포스팅](https://algopoolja.tistory.com/80)에서 다양한 Spring Annotation을 다루었으므로 이후 Spring Annotion이 궁금하면 해당 블로그 글을 참고하시면 됩니다.

```java
package com.purple.purplebook.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}

```

컨트롤러 코드를 이제 다 작성했으니 테스트 코드를 작성해보겠습니다.

```java
package com.purple.purplebook.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
}
```



* `private MockMvc mvc`
  * 웹 API 를 테스트할 때 사용합니다.
  * 스프링 MVC 테스트의 시작점입니다.
  * 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트를 할 수 있습니다.
* `mvc.perform(get("/hello"))`
  * MockMvc를 통해 /hello 주소로 HTTP GET 요청을 합니다.
  * 체이닝(Chaining) 이 되기 때문에 아래와 같이 여러 검증 기능을 이어서 선언할 수 있습니다.
* `.andExpect(status().isOk())`
  * `mvc.perform` 의 결과를 검증합니다.
  * HTTP Header 의 status를 검증합니다.
  * `isOk()` 는 200인지 아닌지를 검증합니다.
* `.andExpect(content().string(hello))`
  * mvc.perform의 결과를 검증합니다.
  * 응답 본문의 내용을 검증합니다.
  * Controller에서 "hello" 를 리턴하기 때문에 이 값이 맞는지 검증합니다.

위 코드대로 실행해보면 우리가 원하는 대로 테스트 코드가 잘 돌아가는것을 확인 할 수 있습니다. 참고로 **수동으로 검증하고 테스트 코드를 작성하지는 않습니다.** 역의 경우(테스트 코드 작성 -> 수동 검증) 는 충분히 가능합니다.





#### HelloController 코드 롬복으로 전환하기

이제 Lombok 을 사용한 DTO 를 만들어 보고 이와 관련된 테스트 코드도 만들어보겠습니다.

```java
package com.purple.purplebook.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HelloResponseDto {

    private final String name;
    private final int amount;

}

```

```java
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
```

참고로 테스트 코드의 `assertThat` 을 사용할 땐 Junit 의 `assertThat` 보다 `assertj` 의 `assertThat` 을 사용하는게 더 좋습니다.

1. `CoreMatchers` 와 달리 추가적인 라이브러리가 필요하지 않습니다.
   * Junit 의 `assertThat` 을 쓰게 되면 `is()` 와 같이 `CoreMatchers` 라이브러리가 필요합니다.
2. 자동완성이 좀 더 확실하게 지원됩니다.
   * IDE에서는 `CoreMathers` 와 같은 `Matcher` 라이브러리의 자동완성 지원이 약합니다.

자세한 설명은 [백기선님의 유튜브](http://bit.ly/30vm9Lg)를 참조하면 더 좋습니다.



위 테스트 코드가 잘 작동하는걸 확인했습니다. 이제 `HelloController` 에도 새로 만든 `ResponseDto` 를 사용하도록 코드를 추가하겠습니다.

```java
package com.purple.purplebook.web;

import com.purple.purplebook.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }


    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }
}

```

위와 같이 `/hello/dto` 로 들어오게 되면 Dto를 리턴하도록 했습니다.

```java
    
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
  ...
}
```

* param
  * API 테스트 할 때 사용될 요청 파라미터를 설정합니다.
  * 단, 값은 String만 허용됩니다.
  * 그래서 숫자/날짜 등의 데이터도 등록할 때는 문자열로 변경해야만 가능합니다. 

* jsonPath
  * JSON 응답값을 필드별로 검증할 수 있는 메소드입니다.
  * $를 기준으로 필드명을 명시합니다.
  * 여기서는 name과 amount를 검증하니 $.name, $.amount 로 검증합니다.
