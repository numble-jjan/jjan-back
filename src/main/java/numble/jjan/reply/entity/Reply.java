package numble.jjan.reply.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.jjan.util.BaseTimeEntity;

@Getter
@NoArgsConstructor
@Entity
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String postId;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String content;

    private String parentId; 
    
    private int likes;
    
    private int reReplyYn;
    
    @Builder
    public Reply(Long id, String postId, String author, String content, String parentId, int likes, int reReplyYn) {
        this.id = id;
    	this.postId = postId;
        this.author = author;
        this.content = content;
        this.parentId = parentId;
        this.likes = likes;
        this.reReplyYn = reReplyYn;
    }

    public void update(String content, int like) {
        this.content = content;
        this.likes = likes;
    }
}
