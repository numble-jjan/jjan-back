package numble.jjan.post.controller;

import io.swagger.annotations.ApiOperation;
import numble.jjan.post.dto.PostListResponseDto;
import numble.jjan.post.dto.PostResponseDto;
import numble.jjan.post.dto.PostSaveRequestDto;
import numble.jjan.post.dto.PostUpdateRequestDto;
import numble.jjan.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    @ApiOperation(value = "게시글 작성")
    public Long save(@RequestParam String categoryId,
                     @RequestParam String title,
                     @RequestParam String content,
                     @RequestParam String location,
                     @RequestParam Double latitude,
                     @RequestParam Double longitude,
                     MultipartHttpServletRequest request) throws IOException {
        String nickName = request.getHeader("nickName");
        PostSaveRequestDto postSaveRequestDto = PostSaveRequestDto.builder()
                .categoryId(categoryId)
                .author(nickName)
                .title(title)
                .content(content)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return postService.save(postSaveRequestDto, request);
    }

    @PutMapping("/post/{id}")
    @ApiOperation(value = "게시글 수정")
    public Long update(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto) {
        //Todo 게시글 수정시 첨부파일도 수정할 수 있는 로직 추가
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
        //Todo 게시글 삭제 시 첨부파일도 삭제하는 로직 추가
        postService.delete(id);
        return id;
    }
}
