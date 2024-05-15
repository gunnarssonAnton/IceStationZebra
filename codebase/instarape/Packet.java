
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Packet implements Serializable {
    byte[] image;
    String text;
    public Packet(BufferedImage bufferedImage, String text){;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
//            ImageIO.write(image, "JPEG", outputStream);
            ImageIO.write(bufferedImage,"JPEG",outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        image = outputStream.toByteArray();
        this.text = text;
    }
}
