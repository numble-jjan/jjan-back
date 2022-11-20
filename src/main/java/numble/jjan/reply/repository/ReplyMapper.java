package numble.jjan.reply.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import numble.jjan.reply.dto.ReplyListResponseDto;

@Mapper
public interface ReplyMapper {
	List<ReplyListResponseDto> findReplyList(String postId);
	List<ReplyListResponseDto> findRereplyList(String postId, String parentId);	
}
