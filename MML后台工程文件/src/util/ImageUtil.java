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
      
    //图片转化成base64字符串  
    public static String imageToBase64(String path) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
            byte[] data = null;
            // 读取图片字节数组
            try {

                InputStream in = new FileInputStream(path);

                data = new byte[in.available()];

                in.read(data);

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
                // 返回Base64编码过的字节数组字符串
            return encoder.encode(data);

    }
}