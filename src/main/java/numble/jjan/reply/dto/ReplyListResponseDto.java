package numble.jjan.reply.dto;

import org.json.simple.JSONArray;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyListResponseDto {
	private Long id;
    private String author;
    private String content;
    private int likes;
    private String modifiedDate;
    private String reReplyYn;
    private JSONArray reply;

    
    /*public ReplyListResponseDto(Reply entity) {
        this.id = entity.getId();
        this.author = entity.getAuthor();
        this.content = entity.getContent();
        this.likes = entity.getLikes();
        this.modifiedDate = entity.getModifiedDate();
    }*/
}
