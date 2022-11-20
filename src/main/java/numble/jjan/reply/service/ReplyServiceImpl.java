package numble.jjan.reply.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import numble.jjan.reply.dto.ReplyListResponseDto;
import numble.jjan.reply.dto.ReplySaveRequestDto;
import numble.jjan.reply.dto.ReplyUpdateRequestDto;
import numble.jjan.reply.entity.Reply;
import numble.jjan.reply.repository.ReplyMapper;
import numble.jjan.reply.repository.ReplyRepository;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService{

	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Override
	public List<ReplyListResponseDto> findReplyList(String postId) {
		return replyMapper.findReplyList(postId);
	}

	@Transactional
	@Override
	public List<ReplyListResponseDto> findRereplyList(String postId, String parentId) {
		return replyMapper.findRereplyList(postId, parentId);
	}
	
	@Transactional
	@Override
	public Long saveReply(ReplySaveRequestDto replySaveRequestDto) {
		// TODO Auto-generated method stub
		return replyRepository.save(replySaveRequestDto.toEntity()).getId();
	}

	@Transactional
	@Override
	public Long updateReply(Long id, ReplyUpdateRequestDto replyUpdateRequestDto) {
		// TODO Auto-generated method stub
		Reply reply = replyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. " + id));
		reply.update(replyUpdateRequestDto.getContent(), replyUpdateRequestDto.getLikes());
		return id;
	}

	@Transactional
	@Override
	public Long deleteReply(Long id) {
		// TODO Auto-generated method stub
		Reply reply = replyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. " + id));
		replyRepository.delete(reply);
		return id;
	}
}
