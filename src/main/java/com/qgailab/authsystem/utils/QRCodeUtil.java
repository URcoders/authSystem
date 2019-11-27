package com.qgailab.authsystem.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;

/**
 * @author linxu
 * @date 2019/11/12
 * <tip>take care of yourself.everything is no in vain.</tip>
 * <p>
 * this util is for generate QR code.
 * </p>
 */
public class QRCodeUtil {
    /**
     * 设定二维码的宽度
     */
    private static final int WIDTH = 500;
    /**
     * 设定二维码的高度
     */
    private static final int HEIGHT = 500;
    /**
     * 设定二维码的图片格式
     */
    private static final String FORMAT = "png";

    /**
     * we can use this to write a QRCode to a Stream.
     * <code>
     * MatrixToImageWriter.writeToStream(matrix, fileName, stream);
     * </code>
     *
     * @param url  go address
     * @param dir  generate dir
     * @param name file name
     * @throws IOException may throws a ex which is dir not found.
     */
    public static void create(String url, String dir, String name) throws IOException {
        //create a map to store some properties. initial capacity is 3,include 3 prop.
        Map<EncodeHintType, Object> properties = new HashMap<>(3);
        //set encode format.
        properties.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //设定图像的错误校正程度：
        /* L = ~7% correction */
        //      L(0x01),
        /* M = ~15% correction */
        //       M(0x00),
        /* Q = ~25% correction */
        //       Q(0x03),
        /* H = ~30% correction */
        //      H(0x02);
        properties.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设定图像外边距（像素）
        properties.put(EncodeHintType.MARGIN, 2);
        try {
            //do encode and get a obj which is package some bits.
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, properties);
            Path file = new File(dir, name + ".png").toPath();
            //using default setting, make bits by a specified format and write to a file(picture).
            MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码
     * @param url 连接
     */
    public static String  create(String url) {
        try {

            // 设置字符集编码
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 生成二维码矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedImage image = toBufferedImage(bitMatrix);

            ImageIO.write(image, "png", out);

            byte[] bytes = out.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            String binary = encoder.encodeBuffer(bytes).trim();

            binary = binary.replaceAll("\r|\n", "");

            binary = "data:image/jpeg;base64," + binary;

            return binary;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * test
     */
    public static void main(String[] args) {
        try {
            create("URL/user=sgdfhjgsdgfsdvbvsdfg2374678fhgsdf", ".", "lx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
