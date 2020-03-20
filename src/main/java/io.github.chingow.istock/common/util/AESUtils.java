package io.github.chingow.istock.common.util;

import org.bson.internal.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * AESUtils
 *
 * @author yangpeng
 * @date 2020/3/10 22:41
 */
public class AESUtils {

    /**
     * 算法名称
     */
    final static String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/模式/填充方式
     */
    final static String algorithmStr = "AES/ECB/PKCS7Padding";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * encrypt input text
     *
     * @param input
     * @param key
     * @return
     */
    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);

            Cipher cipher = Cipher.getInstance(algorithmStr);
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        return new String(Base64.encode(crypted));
    }

    /**
     * decrypt input text
     *
     * @param input
     * @param key
     * @return
     */
    public static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(algorithmStr);
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decode(input));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(output);
    }
}
