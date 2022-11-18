package numble.jjan.file;

import lombok.extern.slf4j.Slf4j;
import numble.jjan.post.dto.PostSaveRequestDto;
import numble.jjan.post.entity.PostFile;
import numble.jjan.user.entity.User;
import numble.jjan.util.exception.FileSaveException;
import numble.jjan.util.exception.MIMETypeException;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class JjanFile {

    Environment env;

    Tika tika;

    @Autowired
    public JjanFile(Environment env) {
        this.env = env;
        this.tika = new Tika();
    }

    /**
     * 파일 확장자 검사
     */
    public String getFileType(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String[] imageExt = {"jpg", "jpeg", "png", "gif"};
        if (Arrays.asList(imageExt).contains(ext)) {
            return "IMAGE";
        }
        return "FILE";
    }

    /**
     * 업로드 파일에 새 파일명 부여
     */
    public String getNewFileName(String fileName, boolean isMacOs) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String now = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        // 올바르지 않은 파일명 검사
        String unSecuredString = "!\"#$%&'()*+,:;<=>?@[]^`{]+$";
        Pattern unSecuredCharPattern = Pattern.compile(unSecuredString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = unSecuredCharPattern.matcher(fileName);
        fileName = matcher.replaceAll("");

        // 맥OS 일때 파일명 처리
        if (isMacOs) {
            fileName = Normalizer.normalize(fileName, Normalizer.Form.NFC);
        }
        StringBuilder sb = new StringBuilder();

        sb.append(now);
        sb.append("_");
        sb.append(convertSpaceToUnderScore(fileName));
        return sb.toString();
    }

    public List<MultipartFile> getMultipartFile(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>();

        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            multipartFiles.add(request.getFile(fileName));
        }
        return multipartFiles;
    }

    public Optional<File> getFile(String filePath) throws IOException {
        Optional<File> file = Optional.of(new File(filePath));
        List<String> acceptMimeTypeList = getAcceptMimeTypeList();
        List<String> acceptMimeTypeStartsWith = getAcceptMimeTypeStartsWith();
        String mimeType = "";

        if (file.isPresent()) {
            mimeType = tika.detect(file.get());
        }

        if (isAllowStartWith(mimeType, acceptMimeTypeStartsWith)
                || isAllowMimeType(mimeType, acceptMimeTypeList)) {
            return file;
        }
        return Optional.empty();
    }

    public String getFilePath(PostFile fileEntity) {
        String fileLocation = env.getProperty("save.post_file.path");
        String fileName = fileEntity.getNewFileName();
        makeDirectories(fileLocation);
        System.out.println("getFilePath : " + fileLocation + " " + fileName);
        return fileLocation + File.separator + fileName;
    }

    public String getFilePath(User userEntity) {
        String fileLocation = env.getProperty("save.profile_file.path");
        makeDirectories(fileLocation);
        return fileLocation + userEntity.getId();
    }

    public void makeDirectories(String fileLocation) {

        File directory = new File(fileLocation);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveFiles(String fileLocation, List<MultipartFile> multipartFiles, List<String> newFileNameList) {
        makeDirectories(fileLocation);

        for (int i = 0; i < multipartFiles.size(); i++) {
            InputStream is = null;
            try {
                is = multipartFiles.get(i).getInputStream();
            } catch (IOException e) {
                log.error("InputStream Exception : ", e);
                throw new FileSaveException(e);
            }
            String mimeType = null;
            try {
                mimeType = tika.detect(is);
            } catch (IOException e) {
                log.error("Tika detect Exception : ", e);
                throw new FileSaveException(e);
            }

            if (mimeTypeChecker(mimeType)) {
                String filePath = fileLocation + File.separator + convertSpaceToUnderScore(newFileNameList.get(i));
                try {
                    multipartFiles.get(i).transferTo(Paths.get(filePath));
                } catch (IOException e) {
                    log.error("File Save Exception : ", e);
                    throw new FileSaveException(e);
                }
            } else {
                log.error("MIME Type Check Error {}", mimeType);
                throw new MIMETypeException(String.format("허용 되지 않은 MIME type 입니다. %s", mimeType));
            }
        }
    }

    public String getFileLocationFromDto(PostSaveRequestDto fileDto) {
        return env.getProperty("save.post_file.path") + fileDto.getCategoryId();
    }

    public void deleteFiles(List<String> filePathList) throws IOException {
        filePathList.stream().map(File::new).forEach(File::delete);
    }

    public void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public void deleteDirs(String dirPath) throws IOException {
        File folder = new File(dirPath);
        if (folder.exists()) {
            File[] folder_list = folder.listFiles();

            for (int i = 0; i < folder_list.length; i++) {
                if (folder_list[i].isFile()) {
                    folder_list[i].delete();
                } else {
                    deleteDirs(folder_list[i].getPath());
                }
                folder_list[i].delete();
            }
            folder.delete();
        }
    }

    public boolean isEmpty(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
                return !directory.iterator().hasNext();
            }
        }
        return false;
    }

    public ResponseEntity<Resource> getFileDownload(Optional<File> file, HttpServletRequest request) throws IOException {
        HttpHeaders header = new HttpHeaders();
        Resource resource = new InputStreamResource(new FileInputStream(file.get()));
        String encodedFileName = fileNameEncoder(file.get().getName(), request);

        header.add("Content-Disposition", "attachment; filename=" + encodedFileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(header)
                .contentLength(file.get().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public void moveFiles(String saveFileLocation, String deleteFileLocation, String fileName) throws IOException {
        makeDirectories(saveFileLocation);
        String savaFilePath = saveFileLocation + File.separator + fileName;
        String deleteFilePath = deleteFileLocation + File.separator + fileName;
        Path originPath = Paths.get(deleteFilePath);
        Path newPath = Paths.get(savaFilePath);
        Files.move(originPath, newPath);
        deleteDirs(deleteFileLocation);
    }

    public boolean mimeTypeChecker(@NotBlank String mimeType) {
        List<String> acceptMimeTypeList = getAcceptMimeTypeList();
        List<String> acceptMimeTypeStartsWith = getAcceptMimeTypeStartsWith();
        return isAllowStartWith(mimeType, acceptMimeTypeStartsWith) ||
                isAllowMimeType(mimeType, acceptMimeTypeList);
    }

    private List<String> getAcceptMimeTypeList() {
        String equalType = env.getProperty("file.mime_type_list");
        return Arrays.asList(Objects.requireNonNull(equalType).split(","));
    }

    private List<String> getAcceptMimeTypeStartsWith() {
        String startsWith = env.getProperty("file.starts_with");
        return Arrays.asList(Objects.requireNonNull(startsWith).split(","));
    }

    private boolean isAllowStartWith(String mimeType, List<String> acceptMimeTypeStartsWith) {
        return acceptMimeTypeStartsWith.stream().anyMatch(mimeType::startsWith);
    }

    private boolean isAllowMimeType(String mimeType, List<String> acceptMimeTypeList) {
        return acceptMimeTypeList.stream().anyMatch(type -> mimeType.equals("application/" + type));
    }

    public String fileNameEncoder(String fileName, HttpServletRequest request) throws UnsupportedEncodingException {

        String requestHeader = request.getHeader("User-Agent");
        String encodedFileName = "";
        String originalFileName = getOriginalFileName(URLDecoder.decode(fileName, "UTF-8"));

        if (requestHeader.contains("Edge")) {
            encodedFileName = URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (requestHeader.contains("MSIE") || requestHeader.contains("Trident")) {
            encodedFileName = URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (requestHeader.contains("Chrome")) {
            encodedFileName = URLEncoder.encode(originalFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (requestHeader.contains("Opera")) {
            encodedFileName = new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1");
        } else if (requestHeader.contains("Firefox")) {
            encodedFileName = new String(originalFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        return encodedFileName;
    }

    public String getOriginalFileName(String fileName) {
        List<Integer> indexes = findIndexes(fileName);
        return fileName.substring(indexes.get(1) + 1);
    }

    public List<Integer> findIndexes(String fileName) {
        List<Integer> indexList = new ArrayList<Integer>();
        int index = fileName.indexOf("_");
        while (index != -1) {
            indexList.add(index);
            index = fileName.indexOf("_", index + 1);
        }
        return indexList;
    }

    public String convertSpaceToUnderScore(String value) {
        return value.replaceAll(" ", "_").toLowerCase();
    }
}
