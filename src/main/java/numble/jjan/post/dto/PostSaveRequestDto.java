package numble.jjan.post.dto;

import numble.jjan.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {
    private String author;
    private String title;
    private String content;
    private String location;

    @Builder
    public PostSaveRequestDto(String author, String title, String content, String location) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.location = location;
    }

    public Post toEntity() {
        return Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .location(location)
                .build();
    }
}
