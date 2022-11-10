package numble.jjan.post.dto;

import numble.jjan.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponseDto {
    private Long id;
    private String author;
    private String title;
    private LocalDateTime modifiedDate;

    public PostListResponseDto(Post entity) {
        this.id = entity.getId();
        this.author = entity.getAuthor();
        this.title = entity.getTitle();
        this.modifiedDate = entity.getModifiedDate();
    }
}
