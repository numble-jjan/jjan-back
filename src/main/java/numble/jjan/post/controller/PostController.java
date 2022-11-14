package numble.jjan.post.controller;

import io.swagger.annotations.ApiOperation;
import numble.jjan.post.dto.PostListResponseDto;
import numble.jjan.post.dto.PostResponseDto;
import numble.jjan.post.dto.PostSaveRequestDto;
import numble.jjan.post.dto.PostUpdateRequestDto;
import numble.jjan.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    @ApiOperation(value = "게시글 작성")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {
        return postService.save(requestDto);
    }

    @PutMapping("/post/{id}")
    @ApiOperation(value = "게시글 수정")
    public Long update(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto) {
        return postService.update(id, requestDto);
    }

    @GetMapping("/post/{id}")
    @ApiOperation(value = "게시글 상세 조회")
    public PostResponseDto findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/post")
    @ApiOperation(value = "게시글 목록 조회")
    public List<PostListResponseDto> findAllDesc() {
        return postService.findAllDesc();
    }

    @DeleteMapping("/post/{id}")
    @ApiOperation(value = "게시글 삭제")
    public Long delete(@PathVariable Long id) {
        postService.delete(id);
        return id;
    }
}
