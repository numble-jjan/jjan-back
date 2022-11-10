package numble.jjan.security.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUriMapper {

    boolean checkUri(String uri);

    String checkMethod(String uri, String userType);
}
