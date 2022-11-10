package numble.jjan.post.service;

import numble.jjan.post.dto.PostListResponseDto;
import numble.jjan.post.dto.PostResponseDto;
import numble.jjan.post.dto.PostSaveRequestDto;
import numble.jjan.post.dto.PostUpdateRequestDto;
import numble.jjan.post.entity.Post;
import numble.jjan.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Transactional
    @Override
    public Long save(PostSaveRequestDto requestDto) {
        return postRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    @Override
    public Long update(Long id, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getLocation());
        return id;
    }

    @Transactional
    @Override
    public PostResponseDto findById(Long id) {
        Post entity = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        return new PostResponseDto(entity);
    }

    @Transactional
    @Override
    public List<PostListResponseDto> findAllDesc() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        postRepository.delete(post);
    }
}
