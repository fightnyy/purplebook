package com.purple.purplebook.domain.posts;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    void 게시글저장_불러오기() throws Exception {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";
        String author = "fightnyy@gmail.com";

        Posts posts1 = Posts.builder()
                           .title(title)
                           .content(content)
                           .author(author)
                           .build();
        postsRepository.save(posts1);
        //when
        List<Posts> postsList = postsRepository.findAll();
        //then
        Posts foundPosts = postsList.get(0);
        assertThat(foundPosts.getTitle()).isEqualTo(title);
        assertThat(foundPosts.getContent()).isEqualTo(content);
        assertThat(foundPosts.getAuthor()).isEqualTo(author);
    }

    @Test
    void BaseTimeEntity_등록() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.of(2022, 5, 1, 0, 0, 0);
        Posts posts1 = Posts.builder()
                           .title("title")
                           .content("content")
                           .author("author")
                           .build();
        postsRepository.save(posts1);

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        System.out.println(">>>>>>>>> createdDate = " + posts.getCreatedDate() + ", modifiedDate = " + posts.getModifiedDate());
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);

    }
}