package numble.jjan.reply.controller;

import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import numble.jjan.reply.dto.ReplyListResponseDto;
import numble.jjan.reply.dto.ReplySaveRequestDto;
import numble.jjan.reply.dto.ReplyUpdateRequestDto;
import numble.jjan.reply.service.ReplyService;

@RestController
@RequiredArgsConstructor
public class ReplyController {
	
	@Autowired
	private ReplyService replyService;
	
	// 댓글 저장
	@PostMapping("/api/reply")
	public JsonNode saveReply(@RequestBody ReplySaveRequestDto requestDto) {
		System.out.println("test ==================================");
		replyService.saveReply(requestDto);
		return null;
	}
	
	// 댓글 조회
	@SuppressWarnings("unchecked")
	@GetMapping("/api/reply/{postId}")
	public String getReplyList(@PathVariable String postId) {
		List<ReplyListResponseDto> list = replyService.findReplyList(postId);
		JSONArray returnJsonArray = new JSONArray();
		
		for(int i = 0; i < list.size(); i++) {
			if("0".equals(list.get(i).getReReplyYn())) {
				JSONArray replyJsonArray = new JSONArray();
				// 댓글에 대댓글 Array 넣기
				for(int j = i+1; j < list.size(); j++) {
					if("0".equals(list.get(j).getReReplyYn())){
						break;
					} else {
						replyJsonArray.add(list.get(j));
					}
				}
				// 댓글만 추출
				list.get(i).setReply(replyJsonArray);
				returnJsonArray.add(list.get(i));
			}
		}
		// jsonArray to String
		Gson gson = new Gson();
		String returnString = gson.toJson(returnJsonArray);
		return returnString; 
	}
	
	// 댓글 수정
	@PutMapping("/api/reply/{id}")
	public Long updateReply(@PathVariable Long id, @RequestBody ReplyUpdateRequestDto replyUpdateDto) {
		long returnId = replyService.updateReply(id, replyUpdateDto);
		return returnId; 
	}
	
	// 댓글 삭제
	@DeleteMapping("/api/reply/{id}")
	public Long deleteReply(@PathVariable Long id) {
		long returnId = replyService.deleteReply(id);
		return returnId; 
	}
	
}
