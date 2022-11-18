package numble.jjan.post.dto;

import numble.jjan.post.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String author;
    private String title;
    private String content;
    private String location;
    private Double latitude;
    private Double longitude;


    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.author = entity.getAuthor();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.location = entity.getLocation();
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
    }
}
