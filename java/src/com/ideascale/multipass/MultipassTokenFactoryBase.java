package com.ideascale.multipass;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MultipassTokenFactoryBase implements MultipassTokenFactory {
    
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final byte[] IV = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private String siteKey;
    private String apiKey;

    private SecretKeySpec keySpec;
    private IvParameterSpec ivSpec;
    private Base64 base64 = new Base64();
    
    protected MultipassTokenFactoryBase(String siteKey, String apiKey) {
        this.siteKey = siteKey;
        this.apiKey = apiKey;
        
        String salted = this.apiKey + this.siteKey;
        byte[] hash = DigestUtils.sha(salted);
        byte[] saltedHash = new byte[16];
        System.arraycopy(hash,0,saltedHash,0,16); // Necessary?
        keySpec = new SecretKeySpec(saltedHash,"AES");
        ivSpec = new IvParameterSpec(IV);
    }
    
    public String encode(JSONObject jsonobj) {
        try {
            return encode(jsonobj.toString(0));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String encode(String string) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        process(new ByteArrayInputStream(string.getBytes()),out,Cipher.ENCRYPT_MODE);
        byte[] encrypted = out.toByteArray();
        byte[] base64bytes = base64.encode(encrypted);
        String encoded = new String(base64bytes);
        encoded = encoded.replaceAll("\\s+", "").replaceAll("\\=+$", "").replaceAll("\\+", "-").replaceAll("\\/","_");
        return encoded;
    }
    
    private void process(InputStream in, OutputStream out, int mode) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(mode, keySpec, ivSpec);
            CipherOutputStream cout = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) >= 0) {
                cout.write(buffer,0,read);
            }
            cout.close();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject decode(String token) {
        try {
            token = token.replaceAll("_","/");
            token = token.replaceAll("\\-","+");
            token += "=";
            byte[] base64bytes = token.getBytes();
            byte[] encrypted = base64.decode(base64bytes);
            ByteArrayInputStream in = new ByteArrayInputStream(encrypted);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            process(in,out,Cipher.DECRYPT_MODE);
            byte[] decrypted = out.toByteArray();
            String string = new String(decrypted);
            JSONObject json = new JSONObject(string);
            return json;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String time(Date time) {
        return TIME_FORMAT.format(time);
    }
}
