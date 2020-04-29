package cn.ncepu.voluntize;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import static cn.ncepu.voluntize.util.RsaUtils.encrypt;

public class RsaUtil2 {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String key = scanner.next();
        byte[] bytes = Base64.getDecoder().decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("rsa");
        PublicKey publicKey2 = keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
        String data = "1234567";
        byte[] encryptedBytes = encrypt(data.getBytes(), publicKey2);
        System.out.println("加密后：" + new String(Base64.getEncoder().encode(encryptedBytes)));
    }
}
