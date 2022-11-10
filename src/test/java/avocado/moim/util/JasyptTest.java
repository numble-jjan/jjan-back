package avocado.moim.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class JasyptTest {

    @Test
    @DisplayName("jasypt 암호화 복호화")
    public void jasyptBCryptAndEncrypt() {
        // given
        String clientId = "wwwww";
        String clientSecret = "aaaaa";

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        config.setPassword("password");
        encryptor.setConfig(config);

        // when
        String encryptedText1 = encryptor.encrypt(clientId);
        System.out.println("암호화 된 내용1 : " + encryptedText1);

        String encryptedText2 = encryptor.encrypt(clientSecret);
        System.out.println("암호화 된 내용2 : " + encryptedText2);

        String decryptedText1 = encryptor.decrypt(encryptedText1);
        String decryptedText2 = encryptor.decrypt(encryptedText2);

        // then
        assertThat(clientId).isEqualTo(decryptedText1);
        assertThat(clientSecret).isEqualTo(decryptedText2);
    }
}
