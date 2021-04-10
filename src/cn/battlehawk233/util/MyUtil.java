package cn.battlehawk233.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

/**
 * 封装一些其它的功能
 */
public class MyUtil {
    private static final MyUtil instance = new MyUtil();

    public static MyUtil getInstance() {
        return instance;
    }

    //封装Icon
    public Icon getIcon(Class<?> cl, String path) {
        Image img = null;
        try {
            img = ImageIO.read(cl.getResource(path));
            img = img.getScaledInstance(16, 16, 80);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImageIcon(img);
    }
}
