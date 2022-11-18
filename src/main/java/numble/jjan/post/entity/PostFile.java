package numble.jjan.post.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PostFile {

    private Long postId;
    private Long postFileId;
    private String fileType;
    private String originalFileName;
    private String newFileName;

    @Builder
    public PostFile(Long postId, Long postFileId, String fileType, String originalFileName, String newFileName) {
        this.postId = postId;
        this.postFileId = postFileId;
        this.fileType = fileType;
        this.originalFileName = originalFileName;
        this.newFileName = newFileName;
    }
}
