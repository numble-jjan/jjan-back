package numble.jjan.security.service;

import numble.jjan.security.repository.IUriMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UriServiceImpl implements UriService {

    IUriMapper uriMapper;

    @Autowired
    public UriServiceImpl(IUriMapper uriMapper) {
        this.uriMapper = uriMapper;
    }

    @Cacheable(cacheNames = "checkUriAccess")
    @Override
    public boolean checkUriAccess(String uri, String method, String claimsType) {
        log.info("checkUri: {} checkMethod [{}] 에 대한 결과가 캐시에 없어 조회", uri, method);

        String userType;
        boolean isExistUri = checkUri(uri);
        String permittedMethod;
        if (isExistUri) {
            switch (claimsType) {
                case "USER":
                    userType = "user";
                    permittedMethod = checkMethod(uri, userType);
                    break;
                case "ADMIN":
                    userType = "admin";
                    permittedMethod = checkMethod(uri, userType);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + claimsType);
            }
            // permittedMethod가 empty String이 아니고, permittedMethod에 method가 포함되어 있으면 true
            return !"".equals(permittedMethod) && permittedMethod.contains(method);
        }
        return true;
    }

    private boolean checkUri(String uri) {
        return uriMapper.checkUri(uri);
    }

    private String checkMethod(String uri, String userType) {
        return uriMapper.checkMethod(uri, userType);
    }
}
