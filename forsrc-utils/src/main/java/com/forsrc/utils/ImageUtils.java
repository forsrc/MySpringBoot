/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.forsrc.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ImageUtils
 */
public final class ImageUtils {

    private static final String FONT_NAME = "Comic Sans MS";

    private static final int FONT_SIZE = 20;

    private static final int HEIGHT = 20;

    private static final int WIDTH = 60;

    private ImageUtils() {
    }

    /**
     * Gets buffered image.
     *
     * @param code {String} code
     * @return {BufferedImage}
     */
    public static BufferedImage getBufferedImage(String code) {

        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        Graphics g = bufferedImage.getGraphics();

        Random r = new Random();

        g.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));

        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(new Color(0, 0, 0));

        g.setFont(new Font(FONT_NAME, Font.CENTER_BASELINE, FONT_SIZE));

        g.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));

        g.drawString(code, 5, 18);

        for (int i = 0; i < 3; i++) {

            g.drawLine(r.nextInt(WIDTH), r.nextInt(HEIGHT), r.nextInt(WIDTH),
                    r.nextInt(HEIGHT));

            g.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        }
        return bufferedImage;
    }

    /**
     * Gets image.
     *
     * @return {Map<String, BufferedImage>}
     */
    public static Map<String, BufferedImage> getImage() {

        String key = getNumber();
        BufferedImage bufferedImage = getBufferedImage(key);
        Map<String, BufferedImage> map = new HashMap<String, BufferedImage>();
        map.put(key, bufferedImage);
        return map;
    }

    /**
     * Gets input stream.
     *
     * @param bufferedImage {BufferedImage}
     * @return {ByteArrayInputStream}
     * @throws IOException IllegalArgumentException NullPointerException ImageFormatException
     */
    public static ByteArrayInputStream getInputStream(BufferedImage bufferedImage) throws IOException {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpg").next();
        ImageOutputStream out = ImageIO.createImageOutputStream(bao);
        try {
            imageWriter.setOutput(out);
            imageWriter.write(bufferedImage);
            return new ByteArrayInputStream(bao.toByteArray());
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Gets number.
     *
     * @return {String} 4 random number
     */
    public static String getNumber() {

        Random r = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 4; i++) {
            sb.append(getRandomChar(r.nextInt(1)));
        }

        return String.valueOf(sb);
    }

    /**
     * Gets code.
     *
     * @param length {int}
     * @return {String} 4 random code
     */
    public static String getCode(int length) {

        Random r = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            sb.append(getRandomChar(r.nextInt(4)));
        }

        return String.valueOf(sb);
    }

    /**
     * Gets random char.
     *
     * @param x 0: 0~9; 1: A~Z; 2: a~z; other: word
     * @return {char}
     */
    public static char getRandomChar(int x) {

        Random r = new Random();
        char c = '0';

        // 48-57 65-90 97-122 u4e00~9fa5 u9fff
        switch (x) {
            case 0:
                c = (char) (r.nextInt(10) + 48); //0 ~ 9
                break;
            case 1:
                c = (char) (r.nextInt(26) + 97); //A ~ Z
                break;
            case 2:
                c = (char) (r.nextInt(26) + 65); //a ~ z
                break;
            default:
                String s = Integer.toHexString((r.nextInt(0x9fa5 - 0x4e00) + 0x4e00));
                c = (char) Integer.parseInt(s, 16);
                break;
        }

        return c;
    }

}
