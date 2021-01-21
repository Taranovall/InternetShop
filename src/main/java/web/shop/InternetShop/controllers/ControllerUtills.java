package web.shop.InternetShop.controllers;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class ControllerUtills {


    public static String fieldNotEmptyWithReflection(Object obj, Model model, String pageName) {
        Class<?> getClass = obj.getClass();
        try {
            for (Field field : getClass.getDeclaredFields()) {
                if (field != null) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (type.equals(String.class)) {
                        String name = field.getName();
                        String value = field.get(obj).toString().trim();
                        if (value.equals("")) {
                            model.addAttribute(name + "IsEmpty", true);
                        } else if (name.equals("price") && !value.matches("([0-9]+(\\.)?[0-9]*)")) {
                            model.addAttribute("isNotNumber", true);
                        } else {
                            model.addAttribute(name, value.trim());
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (model.containsAttribute("isNotNumber")) {
            return pageName;
        }
        for (String key : model.asMap().keySet()) {
            if (key.toLowerCase().contains("empty")) {
                return pageName;
            }
        }
        return null;
    }

    public static byte[] imageHandler(MultipartFile photo, Model model) {
        if (photo.isEmpty()) {
            model.addAttribute("imageIsEmpty", true);
            return null;
        }
        byte[] imageInByte = null;
        try {
            String contentType = photo.getContentType().substring(6);
            BufferedImage originalImage = ImageIO.read(photo.getInputStream());
            BufferedImage resizeImage = resizeImage(originalImage, originalImage.getType());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizeImage, contentType, baos);
            imageInByte = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageInByte;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(400, 400, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 400, 400, null);
        g.dispose();
        return resizedImage;
    }
}
