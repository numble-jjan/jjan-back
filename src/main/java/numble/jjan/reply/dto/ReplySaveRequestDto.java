package numble.jjan.reply.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.jjan.reply.entity.Reply;

@Getter
@NoArgsConstructor
public class ReplySaveRequestDto {
	private String author;
    private String parentId;
    private String content;
    private int likes;

    @Builder
    public ReplySaveRequestDto(String author, String parentId, String content, int likes) {
    	this.author = author;
    	this.parentId = parentId;
    	this.content = content;
    	this.likes = likes;
    }
    
    public Reply toEntity() {
    	return Reply.builder()
    			.author(author)
    			.content(content)
    			.parentId(parentId)
    			.likes(likes)
    			.build();
    }
}
