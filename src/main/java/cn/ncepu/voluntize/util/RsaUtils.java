package cn.ncepu.voluntize.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtils {

    public static void main(String[] args) throws Exception {

        KeyPair keyPair = genKeyPair(1024);

        //获取公钥，并以base64格式打印出来
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyStr = keyToBase64String(publicKey);
        System.out.println("公钥：" + publicKeyStr);

        //假设此时publicKey经过网络传输给了前端

        //下面模拟前端解码Base64格式的publicKeyStr字符串
        KeyFactory keyFactory = KeyFactory.getInstance("rsa");
//        byte[] publicKeyByte =  publicKey.getEncoded();
        byte[] publicKeyByte = Base64.getDecoder().decode(publicKeyStr);
        PublicKey publicKey2 = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByte));

//        System.out.println(keyToBase64String(publicKey2));

        //前端公钥加密
        String data = "123456";
        byte[] encryptedBytes = encrypt(data.getBytes(), publicKey2);

//        System.out.println("加密后：" + new String(encryptedBytes));
        String encry = new String(Base64.getEncoder().encode(encryptedBytes));
        System.out.println("加密后：" + encry);

        //获取私钥，并以base64格式打印出来
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("私钥：" + keyToBase64String(privateKey));

        //加密结果传回后台，后台使用Base64解码

        //私钥解密
        byte[] decryptedBytes = decrypt(Base64.getDecoder().decode(encry), privateKey);
        System.out.println("解密后：" + new String(decryptedBytes));
    }

    //生成密钥对
    public static KeyPair genKeyPair(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keyLength);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //公钥加密
    public static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");//java默认"RSA"="RSA/ECB/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    //私钥解密
    public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    /**
     * 这个方法将key.getEncoded()转成String，它的返回值不能使用String.getBytes()方法解码。
     */
    public static String keyToBase64String(Key key){
        return new String(Base64.getEncoder().encode(key.getEncoded()));
    }
}
