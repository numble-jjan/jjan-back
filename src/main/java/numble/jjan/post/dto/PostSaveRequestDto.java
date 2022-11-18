package numble.jjan.post.dto;

import numble.jjan.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private int categoryId;
    private String author;
    private String title;
    private String content;
    private String location;
    private Double latitude;
    private Double longitude;

    @Builder
    public PostSaveRequestDto(String categoryId, String author, String title, String content, String location, Double latitude, Double longitude) {
        this.categoryId = categoryId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Post toEntity() {
        return Post.builder()
                .categoryId(categoryId)
                .author(author)
                .title(title)
                .content(content)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
