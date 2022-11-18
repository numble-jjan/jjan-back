package numble.jjan.post.repository;

import numble.jjan.post.entity.Post;
import numble.jjan.post.entity.PostFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IPostRepository {

    boolean postSave(Post postEntity);

    boolean postFileSave(List<PostFile> postFileEntityList);
}
