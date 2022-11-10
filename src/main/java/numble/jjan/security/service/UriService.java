package numble.jjan.security.service;

public interface UriService {

    boolean checkUriAccess(String uri, String method, String claimsType);
}
