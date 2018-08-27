package ru.frozen.gitextractor.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Crypto {

    private final String ALGORITHM = "AES";
    private final String TRANSFORMATION = "AES";

    private String prepareKey(String key) {
        if (key.length() > 16) {
            return key.substring(0, 15);
        } else {
            int repeats = 16 - key.length();
            String additional = "a";
            String repeated = new String(new char[repeats]).replace("\0", additional);
            return (repeated + key);
        }
    }

    public void fileProcessor ( int cipherMode, String key, File inputFile, File outputFile){
            try {
                String actualKey = prepareKey(key);
                Key secretKey = new SecretKeySpec(actualKey.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(cipherMode, secretKey);

                FileInputStream inputStream = new FileInputStream(inputFile);
                byte[] inputBytes = new byte[(int) inputFile.length()];
                inputStream.read(inputBytes);

                byte[] outputBytes = cipher.doFinal(inputBytes);
                inputFile.delete();

                FileOutputStream outputStream = new FileOutputStream(outputFile);

                outputStream.write(outputBytes);

                inputStream.close();
                outputStream.close();

            } catch (NoSuchPaddingException | NoSuchAlgorithmException
                    | InvalidKeyException | BadPaddingException
                    | IllegalBlockSizeException | IOException e) {
                e.printStackTrace();
            }
        }
    }
