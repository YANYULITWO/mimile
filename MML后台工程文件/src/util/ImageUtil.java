package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  

import java.util.Base64.Encoder;
import java.util.jar.Attributes.Name;

import org.bouncycastle.util.encoders.Base64Encoder;

import sun.misc.BASE64Encoder;

import java.util.Base64.Decoder;
  
public class ImageUtil
{  
      
    //ͼƬת����base64�ַ���  
    public static String imageToBase64(String path) {
        // ��ͼƬ�ļ�ת��Ϊ�ֽ������ַ��������������Base64���봦��
            byte[] data = null;
            // ��ȡͼƬ�ֽ�����
            try {

                InputStream in = new FileInputStream(path);

                data = new byte[in.available()];

                in.read(data);

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // ���ֽ�����Base64����
            BASE64Encoder encoder = new BASE64Encoder();
                // ����Base64��������ֽ������ַ���
            return encoder.encode(data);

    }
}