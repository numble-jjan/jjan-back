package numble.jjan.post.service;

import numble.jjan.file.JjanFile;
import numble.jjan.post.dto.PostListResponseDto;
import numble.jjan.post.dto.PostResponseDto;
import numble.jjan.post.dto.PostSaveRequestDto;
import numble.jjan.post.dto.PostUpdateRequestDto;
import numble.jjan.post.entity.Post;
import numble.jjan.post.entity.PostFile;
import numble.jjan.post.repository.IPostRepository;
import numble.jjan.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import numble.jjan.util.exception.FileSaveException;
import numble.jjan.util.exception.MIMETypeException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final IPostRepository postFileMapper;
    private final JjanFile jjanFile;

    @Transactional(rollbackFor = {FileSaveException.class, MIMETypeException.class})
    @Override
    public Long save(PostSaveRequestDto requestDto, MultipartHttpServletRequest request) throws IOException {
        List<MultipartFile> multipartFiles = jjanFile.getMultipartFile(request);
        Optional<PostResponseDto> postResponseDto;
        List<String> newFileNameList = new ArrayList<>();
        List<PostFile> postFileEntityList = new ArrayList<>();

        String agent = request.getHeader("USER_AGENT");
        boolean isMacOs = agent.contains("mac");
        Post postEntity = Post.from(requestDto);
        boolean result = postFileMapper.postSave(postEntity);
        Long postId = postEntity.getId();

        // 첨부파일이 있으면
        if (multipartFiles.size() > 0 && result) {
            for (MultipartFile multipartFile : multipartFiles) {
                String newFileName = jjanFile.getNewFileName(multipartFile.getOriginalFilename(), isMacOs);
                newFileNameList.add(newFileName);
                String originalFileName = multipartFile.getOriginalFilename();
                postFileEntityList.add(PostFile.builder()
                        .postId(postId)
                        .fileType(jjanFile.getFileType(Objects.requireNonNull(originalFileName)))
                        .originalFileName(originalFileName)
                        .newFileName(newFileName)
                        .build());
            }

            result = postFileMapper.postFileSave(postFileEntityList);
            if (result) {
                String fileLocation = jjanFile.getFileLocationFromDto(requestDto);
                jjanFile.saveFiles(fileLocation, multipartFiles, newFileNameList);
            }
        }
        return postId;
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
