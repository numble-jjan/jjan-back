package avocado.moim.post.controller;

import numble.jjan.post.dto.PostSaveRequestDto;
import numble.jjan.post.entity.Post;
import numble.jjan.post.repository.PostRepository;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @After("")
    public void tearDown() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String location = "location";
        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(title)
                .content(content)
                .location(location)
                .author("마이클 잭슨")
                .build();

        String url = "http://localhost:" + port + "/post";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getLocation()).isEqualTo(location);
    }
}