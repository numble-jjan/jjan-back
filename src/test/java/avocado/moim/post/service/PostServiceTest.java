package avocado.moim.post.service;

import numble.jjan.post.entity.Post;
import numble.jjan.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @Transactional
    @DisplayName("게시글 저장 & 조회")
    void saveTest() {
        // given
        String author = "2재용";
        String title = "여러분!";
        String content = "삼전은 안접합니다! 모두 안심하고 삼전을 사주십쇼!";

        postRepository.save(Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .build());

        // when
        List<Post> postList = postRepository.findAll();

        // then
        Post post = postList.get(0);
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    @Transactional
    @DisplayName("게시글 삭제")
    void deleteTest() {
        // given
        String author = "이재용";
        String title = "여러분!";
        String content = "삼전은 안접합니다! 모두 안심하고 삼전을 사주십쇼!";

        Long id = postRepository.save(Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .build()).getId();

        // when
        Post entity = postRepository.findById(id).get();
        postRepository.delete(entity);

        // then
        List<Post> post = postRepository.findAll();
        assertThat(post).isEmpty();
    }
}
