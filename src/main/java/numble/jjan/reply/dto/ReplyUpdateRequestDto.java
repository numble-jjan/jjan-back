package numble.jjan.reply.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReplyUpdateRequestDto {
	private String content;
	private int likes;
	
	@Builder
    public ReplyUpdateRequestDto(String content, int likes) {
        this.likes = likes;
        this.content = content;
    }
}
