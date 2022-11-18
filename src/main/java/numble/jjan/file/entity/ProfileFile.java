package numble.jjan.file.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ProfileFile {

    private Long userId;
    private Long userFileId;
    private String fileType;
    private String originalFileName;
    private String newFileName;

    @Builder
    public ProfileFile(Long userId, Long userFileId, String fileType, String originalFileName, String newFileName) {
        this.userId = userId;
        this.userFileId = userFileId;
        this.fileType = fileType;
        this.originalFileName = originalFileName;
        this.newFileName = newFileName;
    }
}
