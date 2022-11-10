package numble.jjan.util;

public interface TokenProperties {
    String SECRET = "numble";
    int EXPIRATION_TIME = 60000 * 10;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "AccessToken";
}
