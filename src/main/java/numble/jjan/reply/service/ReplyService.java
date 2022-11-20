package numble.jjan.reply.service;

import java.util.List;

import numble.jjan.reply.dto.ReplyListResponseDto;
import numble.jjan.reply.dto.ReplySaveRequestDto;
import numble.jjan.reply.dto.ReplyUpdateRequestDto;

public interface ReplyService {
	List<ReplyListResponseDto> findReplyList(String postId);
	List<ReplyListResponseDto> findRereplyList(String postId, String parentId);
	Long saveReply(ReplySaveRequestDto replySaveRequestDto);
	Long updateReply(Long id, ReplyUpdateRequestDto replyUpdateRequestDto);
	Long deleteReply(Long id);
}
