/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.oar.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Clase que maneja las imágenes
 */
public class ImageUtil {

    /**
     * default constructor
     */
    public ImageUtil() {
    }

    /**
     * Obtiene los bytes de una imagen
     *
     * @param imagen
     * @return
     */
    public static byte[] toByte(BufferedImage imagen) {

        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        

        byte[] resultado = new byte[ancho * alto * 3];

        int c = 0;
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                Color col = new Color(imagen.getRGB(i, j));
                resultado[c] = (byte) col.getRed();
                resultado[c + 1] = (byte) col.getGreen();
                resultado[c + 2] = (byte) col.getBlue();
                c += 3;
            }
        }

        return resultado;
    }

    /**
     * Obtiene de un arreglo de bytes la imagen. Por defecto se supone que la imagen
     * tiene una resolución de 32x32
     *
     * @param array
     * @return
     */
    public static BufferedImage toBufferedImage(byte[] array) {

        int iw = 32;
        int ih = 32;
        
        return toBufferedImage(array, iw, ih);
    }

     public static BufferedImage toBufferedImage(byte[] array, int weigth, int height) {

        

        BufferedImage buffer = new BufferedImage(weigth, height, BufferedImage.TYPE_INT_RGB);

        int c = 0;
        int r, g, b;
        for (int i = 0; i < weigth; i++) {
            for (int j = 0; j < height; j++) {
                if (array[c] < 0) {
                    r = 128 + array[c] + 128;
                } else {
                    r = array[c];
                }
                if (array[c + 1] < 0) {
                    g = 128 + array[c + 1] + 128;
                } else {
                    g = array[c + 1];
                }
                if (array[c + 2] < 0) {
                    b = 128 + array[c + 2] + 128;
                } else {
                    b = array[c + 2];
                }
                buffer.setRGB(i, j, (new Color(r, g, b)).getRGB());
                c += 3;
            }
        }
        return buffer;
    }
    /**
     * Verifica que la imagen tenga las dimensiones de 32x32
     *
     * @param img
     * 
     * @return
     */
    public static boolean isValidImg(ImageIcon img) {
        return (img.getIconWidth() == 32 && img.getIconHeight() == 32);
    }

    /**
     * Para escalar una imagen
     *
     * @param img
     * @param targetWidth
     * @param targetHeight
     * @param hint
     * @param progressiveBilinear
     * @return
     */
    public static BufferedImage getFasterScaledInstance(BufferedImage img,
            int targetWidth, int targetHeight, Object hint,
            boolean progressiveBilinear) {
        int type = (img.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;
        int w, h;
        int prevW = ret.getWidth();
        int prevH = ret.getHeight();
        if (progressiveBilinear) {
// Use multistep technique: start with original size,
// then scale down in multiple passes with drawImage()
// until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
// Use one-step technique: scale directly from original
// size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (progressiveBilinear && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }
            if (progressiveBilinear && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }
            if (scratchImage == null) {
// Use a single scratch buffer for all iterations
// and then copy to the final, correctly sized image
// before returning
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    hint);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);
        if (g2 != null) {
            g2.dispose();
        }
// If we used a scratch buffer that is larger than our
// target size, create an image of the right size and copy
// the results into it
        if (targetWidth != ret.getWidth()
                || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth,
                    targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }
        return ret;
    }

    /**
     * Convierte un objeto Image a BufferedImage
     *
     * @param oldImage
     * @return
     */
    public static BufferedImage makeBufferedImage(Image oldImage) {

        // Query the old image for its dimensions
        int w = oldImage.getWidth(null);
        int h = oldImage.getHeight(null);
        // Assume we have a handle to a GraphicsConfig object
        // Create a compatible image
        BufferedImage bImg = createCompatibleImage(w, h);// Get the image Graphics
        Graphics g = bImg.getGraphics();
        // Copy the contents from the old image into the new one
        g.drawImage(oldImage, 0, 0, null);
        // dispose the temporary Graphics object we used
        g.dispose();
        // Return the BufferedImage
        return bImg;
    }

    private static GraphicsConfiguration getConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
    }

    /**
     * Creates a compatible image of the same dimension and
     * transparency as the given image
     * @param image
     * @return
     */
    public static BufferedImage createCompatibleImage(
            BufferedImage image) {
        return createCompatibleImage(image, image.getWidth(),
                image.getHeight());
    }

    /**
     * Creates a compatible image with the given width and
     * height that has the same transparency as the given image
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createCompatibleImage(
            BufferedImage image, int width, int height) {
        return getConfiguration().createCompatibleImage(width,
                height, image.getTransparency());
    }

    /**
     * Creates an opaque compatible image with the given
     *  width and height
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createCompatibleImage(
            int width, int height) {
        return getConfiguration().createCompatibleImage(width,
                height);
    }

    /**
     * Creates a translucent compatible image with the given
     * width and height
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createCompatibleTranslucentImage(
            int width, int height) {
        return getConfiguration().createCompatibleImage(width,
                height, Transparency.TRANSLUCENT);
    }

    /**
     * Creates a compatible image from the content specified
     * by the resource
     *
     * @param resource
     * @return
     * @throws IOException
     */
    public static BufferedImage loadCompatibleImage(URL resource)
            throws IOException {
        BufferedImage image = ImageIO.read(resource);
        return toCompatibleImage(image);
    }

    /**
     * Creates and returns a new compatible image into which
     * the source image is copied
     * If the source image is already compatible, then the
     * source image is returned
     * This version takes a BufferedImage, but it could be
     * extended to take an Image instead
     * @param image
     * @return
     */
    public static BufferedImage toCompatibleImage(
            BufferedImage image) {
        GraphicsConfiguration gc = getConfiguration();
        if (image.getColorModel().equals(gc.getColorModel())) {
            return image;
        }
        BufferedImage compatibleImage = gc.createCompatibleImage(
                image.getWidth(), image.getHeight(),
                image.getTransparency());
        Graphics g = compatibleImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return compatibleImage;
    }
}
