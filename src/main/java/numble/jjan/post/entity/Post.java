package numble.jjan.post.entity;

import numble.jjan.post.dto.PostSaveRequestDto;
import numble.jjan.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.jjan.util.PostCategory;

import javax.persistence.*;
import java.util.Locale;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int categoryId;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private int hitCount;

    private String location;

    private Double latitude;

    private Double longitude;

    @Builder
    public Post(int categoryId, String author, String title, String content, int hitCount, String location, Double latitude, Double longitude) {
        this.categoryId = categoryId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.hitCount = hitCount;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void update(String title, String content, String location) {
        this.title = title;
        this.content = content;
        this.location = location;
    }

    public static Post from(PostSaveRequestDto modifyDto) {
        return Post.builder()
                .categoryId(modifyDto.getCategoryId())
                .author(modifyDto.getAuthor())
                .title(modifyDto.getTitle())
                .content(modifyDto.getContent())
                .location(modifyDto.getLocation())
                .latitude(modifyDto.getLatitude())
                .longitude(modifyDto.getLongitude())
                .build();
    }
}
