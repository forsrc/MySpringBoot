package com.forsrc.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * The type Md 5 utils.
 */
public class MD5Utils {

    /**
     * The constant MAX_BUFFER_SIZE.
     */
    public static final int MAX_BUFFER_SIZE = 1024 * 1024 * 1;
    /**
     * The constant FILE_DEF_EXT.
     */
    public final static String FILE_DEF_EXT = ".json.md5"; //$NON-NLS-1$
    /**
     * The constant MAX_BUFFER.
     */
    public final static int MAX_BUFFER = 1024 * 1024;
    /**
     * The constant isSaveMd5Info.
     */
    public static boolean isSaveMd5Info = true;
    /**
     * The constant isShowProgress.
     */
    public static boolean isShowProgress = false;

    /**
     * Md 5 string.
     *
     * @param string the string
     * @return String string
     * @throws
     * @Title: md5
     * @Description:
     */
    public static String md5(String string) {
        if (string == null) {
            return null;
        }
        return DigestUtils.md5Hex(string);
    }

    /**
     * Md 5 dir.
     *
     * @param dir the dir
     * @return void
     * @throws IOException the io exception
     * @Title: md5dir
     * @Description:
     */
    public static void md5dir(File dir) throws IOException {
        if (dir == null || !dir.exists()) {
            // LogUtils.logger.debug(dir + "(Not exists)");
            return;
        }
        if (dir.isFile()) {
            md5(dir);
            return;
        }
        Set<File> list = new HashSet<File>();
        File[] files = dir.listFiles();
        for (File f : files) {
            String fileName = f.getAbsolutePath();
            if (f.isDirectory()) {
                list.add(f);
                continue;
            }
            if (fileName.endsWith(".md5")) {
                continue;
            }
            md5(f);
        }
        Iterator<File> it = list.iterator();
        while (it.hasNext()) {
            md5dir(it.next());
        }
    }

    /**
     * Md 5 thread.
     *
     * @param file the file
     * @param pool the pool
     * @throws IOException the io exception
     */
    public static void md5Thread(File file, ExecutorService pool)
            throws IOException {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            md5(file);
            return;
        }

        try {
            md5DirThread(file, pool);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    private static void md5DirThread(File dir, ExecutorService pool)
            throws IOException {
        if (dir.isFile()) {
            return;
        }

        File[] files = dir.listFiles();
        for (File f : files) {
            String fileName = f.getPath();
            if (f.isDirectory()) {
                md5DirThread(new File(fileName), pool);
                continue;
            }
            if (fileName.endsWith(".md5")) {
                continue;
            }
            if (pool != null) {
                pool.execute(new Md5Thread(f));
            }
        }
    }

    /**
     * Gets file md 5.
     *
     * @param file      the file
     * @param isBigfile the is bigfile
     * @return String file md 5
     * @throws IOException the io exception
     * @Title: getFileMd5
     * @Description:
     */
    public static String getFileMd5_(File file, boolean isBigfile)
            throws IOException {
        if (isBigfile) {
            return getFileMd5(file);
        }
        String md5 = "";
        InputStream in = null;

        try {

            in = new BufferedInputStream(new FileInputStream(file), MAX_BUFFER);

            try {

                md5 = DigestUtils.md5Hex(in);

            } catch (IOException e) {
                throw new IOException(e);
            }
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return md5;
    }

    /**
     * Gets file md 5 x.
     *
     * @param file the file
     * @return the file md 5 x
     * @throws IOException the io exception
     */
    public static String getFileMd5X(File file) throws IOException {
        return getFileMd5(file);
    }

    /**
     * Gets file md 5.
     *
     * @param file the file
     * @return the file md 5
     * @throws IOException the io exception
     */
    public static String getFileMd5(File file) throws IOException {
        if (file == null || !file.exists() || file.isDirectory()) {
            return "";
        }
        File md5File = new File(file.getPath() + FILE_DEF_EXT);
        String json = FileUtils.getFileTxt(md5File);
        String md5 = null;
        if (md5File.exists() && json != null && checkMd5(file)) {
            md5 = (String) JsonUtils.getValue("md5", json);
            if (md5 != null && md5.length() > 0) {
                return md5;
            }
        }
        InputStream in = null;
        long start = System.currentTimeMillis();
        try {

            MessageDigest md = DigestUtils.getMd5Digest();
            in = new BufferedInputStream(new FileInputStream(file), MAX_BUFFER);
            byte[] buffer = new byte[1024 * 1024];
            int length = -1;
            long index = 1;
            long read = 0;
            while ((length = in.read(buffer)) != -1) {
                md.update(buffer, 0, length);
                if (isShowProgress
                        && (read = read + length) >= MAX_BUFFER * index) {
                    double rate = read * 100.00 / file.length();
                    double speed = 1.000
                            * read
                            * 1000
                            / (1024 * 1024 * 1.000 * (System
                            .currentTimeMillis() - start));
                    CmdUtils.printMark(new StringBuilder()
                            .append(DateTimeUtils.getDateTime())
                            .append(" [INFO] md5 ")
                            .append(String.format("%5.2f", rate))
                            .append("% ")
                            .append(String.format(
                                    "%" + (file.length() + "").length() + "d",
                                    read)).append("/").append(file.length())
                            .append(" ")
                            .append(String.format("%.3f", speed) + "m/s (")
                            .append(file.getName()).append(")").toString());
                }
            }
            return new String(Hex.encodeHex(md.digest()));

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("GetFileMd5():" + file.getPath(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
    }

    /**
     * Md 5.
     *
     * @param file the file
     * @return String
     * @throws IOException the io exception
     * @Title: md5
     * @Description:
     */
    public static void md5(File file) throws IOException {
        if (file == null || !file.exists()) {
            return;
        }
        long start = System.currentTimeMillis();
        if (file.isDirectory()) {
            md5dir(file);
            return;
        }

        if (checkMd5(file)) {
            // LogUtils.logger.info("md5: " + file.getAbsolutePath() +
            // "  skip.");
            LogUtils.LOGGER.info(new StringBuilder()
                    .append("md5: ")
                    .append(file.getPath())
                    .append(" [Already md5] ")
                    .append(file.length())
                    .append(" md5: ")
                    .append((String) JsonUtils.getValue(
                            "md5",
                            FileUtils.getFileTxt(new File(file.getPath()
                                    + FILE_DEF_EXT)))).toString());

            return;
        }
        // LogUtils.logger.info("md5: " + file.getAbsolutePath() + " ...");

        String md5 = getFileMd5(file);
        if (isSaveMd5Info && md5.length() > 0) {
            // FileUtils .setFileTxt(new File(file.getAbsolutePath() + ".md5"),
            // md5);
            File jsonFile = new File(file.getPath() + FILE_DEF_EXT);
            String json = JsonUtils.setValue("fileName", file.getName(),
                    FileUtils.getFileTxt(jsonFile));

            json = JsonUtils.setValue("md5", md5, json);
            json = JsonUtils.setValue("md5Time", DateTimeUtils.getDateTime(),
                    json);
            json = JsonUtils.setValue("filePath", file.getPath(), json);
            json = JsonUtils.setValue("fileLength", file.length() + "", json);
            json = JsonUtils.setValue("fileLastModifiedTime", DateTimeUtils
                            .getDateTime(file.lastModified(), "yyyy-MM-dd HH:mm:ss"),
                    json);
            json = JsonUtils.setValue("fileLastModified", file.lastModified()
                    + "", json);
            json = JsonUtils.setValue("md5Start1k", getFileMd5(file, 0, 1024L)
                    + "", json);
            json = JsonUtils.setValue("md5End1k",
                    getFileMd5(file, file.length() - 1024, 1024L) + "", json);

            FileUtils.setFileTxt(jsonFile, JsonUtils.jsonToPrintln(json));
        }

        // LogUtils.logger.info("md5: " + file.getPath() + " ["
        // + (System.currentTimeMillis() - start) + "ms] " + md5);
        LogUtils.LOGGER.info(new StringBuilder().append("md5: ")
                .append(file.getPath()).append(" [")
                .append((System.currentTimeMillis() - start)).append("ms] ")
                .append(file.length()).append(" md5: ").append(md5).toString());

    }

    /**
     * Is md 5 file boolean.
     *
     * @param file the file
     * @return the boolean
     */
    public static boolean isMd5File(File file) {
        if (file == null || !file.exists() || file.isDirectory()
                || file.length() <= 0) {
            return false;
        }
        if (isMd5FileType(file.getAbsolutePath()) || checkMd5(file)) {
            return true;
        }

        return false;
    }

    /**
     * Is md 5 file type boolean.
     *
     * @param fileName the file name
     * @return the boolean
     */
    public static boolean isMd5FileType(String fileName) {
        if (fileName == null) {
            return false;
        }
        int length = fileName.length();
        if (length > FILE_DEF_EXT.length() && fileName.endsWith(FILE_DEF_EXT)
                && checkMd5(new File(fileName.replace(FILE_DEF_EXT, "")))) {
            return true;
        }

        return false;
    }

    /**
     * Gets file md 5.
     *
     * @param file  the file
     * @param start the start
     * @return the file md 5
     * @throws IOException the io exception
     */
    public static String getFileMd5(File file, long start) throws IOException {
        RandomAccessFile raf = null;
        long size = 0;
        try {
            raf = new RandomAccessFile(file, "r");
            size = raf.length();

            start = start >= size ? size - MAX_BUFFER_SIZE : start;
            start = start < 0 ? 0 : start;

            MessageDigest md = DigestUtils.getMd5Digest();
            byte[] buf = new byte[MAX_BUFFER_SIZE];

            raf.seek(start);
            int length = 1024 * 1024;

            // System.out.println("L start " + start);
            int read = 0;
            while (read < length) {
                int len = raf.read(buf);
                if (len == -1) {
                    break;
                }
                md.update(buf, 0, (int) (read + len > length ? length : len));
                read += len;
            }

            return new String(Hex.encodeHex(md.digest()));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }

    /**
     * Gets file md 5.
     *
     * @param file   the file
     * @param start  the start
     * @param length the length
     * @return the file md 5
     * @throws IOException the io exception
     */
    public static String getFileMd5(File file, long start, long length)
            throws IOException {
        // RandomAccessFile in = null;
        InputStream in = null;
        long size = 0;
        try {
            // in = new RandomAccessFile(file, "r");
            // size = in.length();
            in = new BufferedInputStream(new FileInputStream(file), 1024 * 2);
            size = file.length();

            start = start >= size ? size - length : start;
            start = start < 0 ? 0 : start;

            MessageDigest md = DigestUtils.getMd5Digest();
            byte[] buf = new byte[1024 * 2];
            // in.seek(start);
            in.skip(start);

            // System.out.println("L start " + start);
            int read = 0;
            while (read < length) {
                int len = in.read(buf);
                if (len == -1) {
                    break;
                }
                md.update(buf, 0, (int) (read + len > length ? length : len));
                read += len;
            }
            // System.out.println("L read  " + read);
            return new String(Hex.encodeHex(md.digest()));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * Gets file md 5 old.
     *
     * @param file   the file
     * @param start  the start
     * @param length the length
     * @return the file md 5 old
     * @throws IOException the io exception
     */
    public static String getFileMd5Old(File file, long start, long length)
            throws IOException {
        RandomAccessFile raf = null;
        long size = 0;
        try {
            raf = new RandomAccessFile(file, "r");
            size = raf.length();

            start = start >= size ? size - length : start;
            start = start < 0 ? 0 : start;

            MessageDigest md = DigestUtils.getMd5Digest();
            byte[] buf = new byte[MAX_BUFFER_SIZE];
            raf.seek(start);
            int len = 0;
            long read = 0;
            long index = 0;
            // System.out.println("L start " + start);
            while ((len = raf.read(buf)) != -1) {
                index++;
                if (len >= length && index == 1) {
                    read = length;
                    md.update(buf, 0, (int) length);
                    break;
                }
                read += len;
                if (read >= length * 2) {
                    int rd = (int) (length - read + len);
                    md.update(buf, 0, (int) (length - read));
                    read = read - rd;
                    break;
                }
                if (read == length) {
                    md.update(buf, 0, len);
                    break;
                }
                if (read > length) {
                    int rd = (int) (length - read + len);
                    md.update(buf, 0, rd);
                    read = read - len + rd;
                    break;
                }
                md.update(buf, 0, len);

            }
            // System.out.println("L read  " + read);
            return new String(Hex.encodeHex(md.digest()));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }

    /**
     * Check md 5 boolean.
     *
     * @param file the file
     * @return the boolean
     */
    public static boolean checkMd5(File file) {
        return checkMd5(file, null);
    }

    /**
     * Check md 5 boolean.
     *
     * @param file the file
     * @param md5  the md 5
     * @return the boolean
     */
    public static boolean checkMd5(File file, String md5) {
        if (file == null || !file.exists()) {
            return false;
        }
        File jsonFile = new File(file.getPath() + FILE_DEF_EXT);
        if (!jsonFile.exists()) {
            return false;
        }
        try {
            String jsonString = FileUtils.getFileTxt(jsonFile);
            if (md5 != null
                    && !md5.equals(JsonUtils.getValue("md5", jsonString))) {
                return false;
            }
            String length = (String) JsonUtils.getValue("fileLength",
                    jsonString);
            if (length == null || !length.equals(file.length() + "")) {
                return false;
            }

            String md5Start1k = (String) JsonUtils.getValue("md5Start1k",
                    jsonString);
            if (md5Start1k == null
                    || !md5Start1k.equals(getFileMd5(file, 0, 1024))) {
                return false;
            }
            String md5End1k = (String) JsonUtils.getValue("md5End1k",
                    jsonString);
            if (md5End1k == null
                    || !md5End1k.equals(getFileMd5(file, file.length() - 1024,
                    1024))) {
                return false;
            }

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static class Md5Thread extends Thread {
        private File file;
        private boolean isShow = false;

        /**
         * Instantiates a new Md 5 thread.
         */
        public Md5Thread() {
        }

        /**
         * Instantiates a new Md 5 thread.
         *
         * @param file the file
         */
        public Md5Thread(File file) {
            this.file = file;
            this.isShow = false;
        }

        @Override
        public void run() {
            if (this.file == null) {
                return;
            }
            try {
                md5(this.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Gets file.
         *
         * @return the file
         */
        public File getFile() {
            return this.file;
        }

        /**
         * Sets file.
         *
         * @param file the file
         */
        public void setFile(File file) {
            this.file = file;
        }
    }

}
