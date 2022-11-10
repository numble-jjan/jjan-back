package numble.jjan.post.entity;

import numble.jjan.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String location;

    @Builder
    public Post(String author, String title, String content, String location) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.location = location;
    }

    public void update(String title, String content, String location) {
        this.title = title;
        this.content = content;
        this.location = location;
    }
}
