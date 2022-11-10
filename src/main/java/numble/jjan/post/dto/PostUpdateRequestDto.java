package numble.jjan.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private String location;

    @Builder
    public PostUpdateRequestDto(String title, String content, String location) {
        this.title = title;
        this.content = content;
        this.location = location;
    }
}
