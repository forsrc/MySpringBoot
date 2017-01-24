package com.forsrc.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

//import org.apache.commons.compress.archivers.zip.Zip64Mode;


/**
 * The type My zip.
 */
public class MyZip {
    private static final String VERSION_DEF = "0.0.1";
    private File zipFile;
    private boolean isAppend = true;
    private boolean isCheck = false;
    private boolean isCover = false;
    private String zipFileMd5;
    private Map<File, String> zipMap = new IdentityHashMap<File, String>();
    private String encode = Charset.defaultCharset().name();
    private int level = -1;
    private int bufferSizipEntry = 1024 * 1024 * 1;
    private String root = "";
    private Map<ZipEntry, Boolean> list = new HashMap<ZipEntry, Boolean>();
    private boolean progress = false;
    private String version = VERSION_DEF;
    private boolean saveMd5Info = true;

    // private Zip64Mode zip64Mode = Zip64Mode.Never;

    /**
     * Instantiates a new My zip.
     *
     * @param zipFile the zip file
     */
    public MyZip(File zipFile) {
        this.zipFile = zipFile;
    }

    /**
     * Add zip entry.
     *
     * @param zipEntry the zip entry
     * @param isSucc   the is succ
     * @return void
     * @throws
     * @Title: addZipEntry
     * @Description:
     */
    public synchronized void addZipEntry(ZipEntry zipEntry, boolean isSucc) {
        if (!this.list.containsKey(zipEntry)) {
            this.list.put(zipEntry, isSucc);
        }
    }

    /**
     * Check zip file boolean.
     *
     * @param onlySize the only size
     * @return the boolean
     * @throws IOException the io exception
     */
    public boolean checkZipFile(boolean onlySize) throws IOException {
        if (this.zipFile == null || !this.zipFile.exists()) {
            return false;
        }

        boolean isSucc = false;
        boolean isJavaZip = false;
        Enumeration<? extends ZipEntry> enumeration = getEntries();
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = enumeration.nextElement();
            if (zipEntry.getName().matches("[\\s\\S]+.javazip.list$")) {
                isJavaZip = true;
                isSucc = checkFileList(zipEntry, onlySize);
            }
        }
        if (isJavaZip) {
            return isSucc;
        }
        enumeration = null;
        ZipFile zip = new ZipFile(this.zipFile);
        enumeration = getEntries();
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = enumeration.nextElement();
            if (zipEntry == null || zipEntry.isDirectory()) {
                continue;
            }
            if (onlySize) {
                String msg = new StringBuffer().append("check ")
                        .append(zipEntry.getName()).append(" size: ")
                        .append(zipEntry.getSize()).toString();
                LogUtils.LOGGER.info(msg);
                isSucc = true;
                continue;
            }
            isSucc = checkZipFile(zip, zipEntry);
        }
        zip.close();
        return isSucc;
    }

    /**
     * Check file list boolean.
     *
     * @param listZipEntry the list zip entry
     * @param onlySize     the only size
     * @return the boolean
     * @throws IOException the io exception
     */
    public boolean checkFileList(ZipEntry listZipEntry, boolean onlySize)
            throws IOException {

        BufferedReader bufferedReader = null;
        boolean isSucc = true;
        ZipFile zip = new ZipFile(this.zipFile);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(zip.getInputStream(listZipEntry),
                            this.bufferSizipEntry)), this.bufferSizipEntry);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                String zipEntryName = (String) JsonUtils.getValue("name", str);
                if (zipEntryName == null) {
                    LogUtils.LOGGER.warn("Wrong comment: " + str);
                    continue;
                }
                ZipEntry zipEntry = zip.getEntry(zipEntryName);
                if (zipEntry == null) {
                    LogUtils.LOGGER.warn("Miss  \"" + zipEntryName + "\"."
                            + str);
                    isSucc = false;
                    continue;
                }
                if (onlySize && !zipEntry.isDirectory()) {
                    String msg = new StringBuffer().append("check ")
                            .append(zipEntry.getName()).append(" size: ")
                            .append(zipEntry.getSize()).toString();

                    String sizeStr = (String) JsonUtils.getValue("length",
                            zipEntry.getComment());
                    if (sizeStr == null || sizeStr.isEmpty()) {
                        LogUtils.LOGGER.warn(msg);
                        isSucc = false;
                        continue;
                    }
                    if (!sizeStr.equals(String.valueOf(zipEntry.getSize()))) {
                        LogUtils.LOGGER.warn(msg);
                        isSucc = false;
                    }
                    LogUtils.LOGGER.info(msg);
                    continue;
                }
                if (!checkZipFile(zip, zipEntry)) {
                    isSucc = false;
                }
            }
        } catch (IOException e) {
            // e.printStackTrace();
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // e.printStackTrace();
                    throw e;
                }
            }
            if (zip != null) {
                zip.close();
            }
        }
        return isSucc;
    }

    /**
     * Check zip file boolean.
     *
     * @param zipFile  the zip file
     * @param zipEntry the zip entry
     * @return the boolean
     * @throws IOException the io exception
     */
    public boolean checkZipFile(ZipFile zipFile, ZipEntry zipEntry)
            throws IOException {

        if (zipEntry == null || zipEntry.isDirectory()) {
            return true;
        }

        boolean isSucc = false;

        InputStream in = null;
        String commentMd5 = null;
        String md5 = null;
        long start = System.currentTimeMillis();
        try {
            commentMd5 = (String) JsonUtils.getValue("md5",
                    zipEntry.getComment());
            in = zipFile.getInputStream(zipEntry);

            byte[] buffer = new byte[bufferSizipEntry];
            int length = -1;
            long count = zipEntry.getSize() / bufferSizipEntry + 1;
            long index = 1;
            long read = 0;
            MessageDigest md = DigestUtils.getMd5Digest();
            while ((length = in.read(buffer)) != -1) {
                md.update(buffer, 0, length);
                if (progress
                        && ((read = read + length) >= bufferSizipEntry * index)) {
                    double d = (index++ * 100.000 / count);
                    double speed = 1.000
                            * read
                            * 1000
                            / (1024 * 1024 * 1.000 * (System
                            .currentTimeMillis() - start));
                    CmdUtils.printMark(DateTimeUtils.getDateTime()
                            + " [INFO] check   "
                            // + zipEntry.getName()
                            // + " -> "
                            // + file.getPath()
                            // + " "
                            + String.format("%5.2f", d)
                            + "% "
                            + String.format(
                            "%" + (zipEntry.getSize() + "").length()
                                    + "d", read) + "/"
                            + zipEntry.getSize() + " "
                            + String.format("%.3f", speed) + "m/s ");
                }
            }
            md5 = new String(Hex.encodeHex(md.digest()));
            if (this.progress) {
                CmdUtils.printMark(DateTimeUtils.getDateTime()
                        + " [INFO] check   "
                        // + zipEntry.getName() + " -> " + file.getPath()
                        + " 100.000% " + read + "/" + zipEntry.getSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        double speed = 1.000 * zipEntry.getSize() * 1000
                / (1024 * 1024 * 1.000 * (System.currentTimeMillis() - start));

        if (commentMd5 == null || (md5 != null && md5.equals(commentMd5))) {
            isSucc = true;
        }
        String msg = new StringBuffer()
                .append(commentMd5 == null ? "check (Without comment) "
                        : "check ")
                .append(zipEntry.getName())
                .append(" size: ")
                .append(zipEntry.getSize())
                .append(progress ? " " + String.format("%.3f", speed) + "m/s"
                        : "").append(" md5: ").append(md5).toString();

        if (isSucc) {
            if (commentMd5 == null) {
                LogUtils.LOGGER.warn(msg);
            } else {
                LogUtils.LOGGER.info(msg);
            }
        } else {
            LogUtils.LOGGER.warn(msg);
        }
        return isSucc;
    }

    /**
     * Gets buffer sizip entry.
     *
     * @return the buffer sizip entry
     */
    public int getBufferSizipEntry() {
        return this.bufferSizipEntry;
    }

    /**
     * Sets buffer sizip entry.
     *
     * @param bufferSizipEntry the buffer sizip entry
     */
    public void setBufferSizipEntry(int bufferSizipEntry) {
        this.bufferSizipEntry = bufferSizipEntry;
    }

    /**
     * Gets encode.
     *
     * @return the encode
     */
    public String getEncode() {
        return this.encode;
    }

    /**
     * Sets encode.
     *
     * @param encode the encode
     */
    public void setEncode(String encode) {
        this.encode = encode;
    }

    /**
     * Gets entries.
     *
     * @return the entries
     * @throws IOException the io exception
     */
    public Enumeration<ZipEntry> getEntries() throws IOException {
        ZipFile zip = null;
        try {
            zip = new ZipFile(this.zipFile);
            ArrayList<ZipEntry> arrayList = new ArrayList<ZipEntry>();
            Enumeration<? extends ZipEntry> enumeration = zip.entries();
            while (enumeration.hasMoreElements()) {
                arrayList.add(enumeration.nextElement());
            }
            return Collections.enumeration(arrayList);
        } catch (IOException e) {
            // e.printStackTrace();
            // throw e;
            return Collections.enumeration(new HashSet<ZipEntry>());
        } finally {
            if (zip != null) {
                zip.close();
            }
        }
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(int level) {
        if (level > -1 && level <= 9) {
            this.level = level;
        }
    }

    /**
     * Gets list.
     *
     * @return the list
     */
    public synchronized Map<ZipEntry, Boolean> getList() {
        return this.list;
    }

    /**
     * Sets list.
     *
     * @param list the list
     */
    public synchronized void setList(Map<ZipEntry, Boolean> list) {
        this.list = list;
    }

    /**
     * Gets root.
     *
     * @return the root
     */
    public String getRoot() {
        return this.root;
    }

    /**
     * Sets root.
     *
     * @param root the root
     */
    public void setRoot(String root) {
        if (root == null) {
            return;
        }
        this.root = root;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets zip entries.
     *
     * @return Enumeration<ZipEntry> zip entries
     * @throws IOException the io exception
     * @Title: getZipEntries
     * @Description:
     */
    public Enumeration<ZipEntry> getZipEntries() throws IOException {
        if (this.zipFile == null || !this.zipFile.exists()) {
            LogUtils.LOGGER.info(this.zipFile + " (Not exist.)");
            return Collections.enumeration(new HashSet<ZipEntry>());
        }
        if (this.list != null && this.list.size() > 0) {
            return Collections.enumeration(this.list.keySet());
        }
        return getEntries();
    }

    /**
     * Gets zip entry.
     *
     * @param zip   the zip
     * @param key   the key
     * @param isReg the is reg
     * @return the zip entry
     */
    public ZipEntry getZipEntry(ZipFile zip, String key, boolean isReg) {
        if (zip == null) {
            return null;
        }
        ZipEntry zipEntry = null;
        Enumeration<? extends ZipEntry> enumeration = zip.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry ze = enumeration.nextElement();
            if (ze.getName().equals(key)
                    || (isReg && ze.getName().matches(key))) {
                zipEntry = ze;
                break;
            }
        }
        if (zipEntry == null) {
            LogUtils.LOGGER.info("Not exist (" + key + ")");
        }
        return zipEntry;
    }

    /**
     * Gets zip entry file.
     *
     * @param zip      the zip
     * @param key      the key
     * @param saveFile the save file
     * @param isReg    the is reg
     * @throws IOException the io exception
     */
    public void getZipEntryFile(ZipFile zip, String key, File saveFile,
                                boolean isReg) throws IOException {
        if (zip == null || saveFile == null) {
            return;
        }
        if (saveFile.getParentFile() != null
                && !saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        Enumeration<? extends ZipEntry> enumeration = zip.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry ze = enumeration.nextElement();
            if (!ze.getName().startsWith(key)
                    && !(isReg && ze.getName().matches(key))) {
                continue;
            }
            File file = new File(saveFile.getPath() + File.separator
                    + ze.getName());
            if (ze.isDirectory()) {
                file.mkdirs();
                continue;
            }
            unzip(zip, ze, saveFile.exists() && saveFile.isDirectory() ? file
                    : saveFile);
        }

    }

    /**
     * Gets zip entry file txt.
     *
     * @param zip   the zip
     * @param key   the key
     * @param isReg the is reg
     * @return the zip entry file txt
     * @throws IOException the io exception
     */
    public String getZipEntryFileTxt(ZipFile zip, String key, boolean isReg)
            throws IOException {
        if (zip == null) {
            return "";
        }
        ZipEntry zipEntry = getZipEntry(zip, key, isReg);
        if (zipEntry == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(zip.getInputStream(zipEntry),
                            this.bufferSizipEntry)), this.bufferSizipEntry);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str).append("\n");
            }
        } catch (IOException e) {
            // e.printStackTrace();
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    // e.printStackTrace();
                    throw e;
                }
            }
        }

        return stringBuffer.toString();
    }

    /**
     * Gets zip file.
     *
     * @return the zip file
     */
    public File getZipFile() {
        return this.zipFile;
    }

    /**
     * Sets zip file.
     *
     * @param zipFile the zip file
     */
    public void setZipFile(File zipFile) {
        this.zipFile = zipFile;
    }

    /**
     * Gets zip file md 5.
     *
     * @return String zip file md 5
     * @throws
     * @Title: getZipFileMd5
     * @Description:
     */
    public String getZipFileMd5() {
        if (this.zipFileMd5 == null || this.zipFileMd5.equals("")) {
            try {
                this.zipFileMd5 = MD5Utils.getFileMd5(this.zipFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.zipFileMd5;
    }

    /**
     * Sets zip file md 5.
     *
     * @param md5 the md 5
     */
    public void setZipFileMd5(String md5) {
        this.zipFileMd5 = md5;
    }

    /**
     * Gets zip map.
     *
     * @return the zip map
     */
    public synchronized Map<File, String> getZipMap() {
        return this.zipMap;
    }

    /**
     * Sets zip map.
     *
     * @param zipMap the zip map
     */
    public synchronized void setZipMap(Map<File, String> zipMap) {
        this.zipMap = zipMap;
    }

    /**
     * Is append boolean.
     *
     * @return the boolean
     */
    public boolean isAppend() {
        return this.isAppend;
    }

    /**
     * Sets append.
     *
     * @param isAppend the is append
     */
    public void setAppend(boolean isAppend) {
        this.isAppend = isAppend;
    }

    /**
     * Is check boolean.
     *
     * @return the boolean
     */
    public boolean isCheck() {
        return this.isCheck;
    }

    /**
     * Sets check.
     *
     * @param isCheck the is check
     */
    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    /**
     * Is cover boolean.
     *
     * @return the boolean
     */
    public boolean isCover() {
        return this.isCover;
    }

    /**
     * Sets cover.
     *
     * @param isCover the is cover
     */
    public void setCover(boolean isCover) {
        this.isCover = isCover;
    }

    /**
     * Is progress boolean.
     *
     * @return the boolean
     */
    public boolean isProgress() {
        return this.progress;
    }

    /**
     * Sets progress.
     *
     * @param progress the progress
     */
    public void setProgress(boolean progress) {
        this.progress = progress;
    }

    /**
     * Is save md 5 info boolean.
     *
     * @return the boolean
     */
    public synchronized boolean isSaveMd5Info() {
        return this.saveMd5Info;
    }

    /**
     * Sets save md 5 info.
     *
     * @param saveMd5Info the save md 5 info
     */
    public synchronized void setSaveMd5Info(boolean saveMd5Info) {
        this.saveMd5Info = saveMd5Info;
    }

    /**
     * Unzip.
     *
     * @return void
     * @throws IOException the io exception
     * @Title: unzip
     * @Description:
     */
    public void unzip() throws IOException {
        if (this.zipFile == null || !this.zipFile.exists()) {
            LogUtils.LOGGER.info(this.zipFile + " (Not exist.)");
            return;
        }
        if (this.root != null && !"".equals(this.root)) {
            new File(this.root).mkdirs();
        }
        ZipFile zip = null;
        try {
            zip = new ZipFile(this.zipFile);
            // Enumeration<ZipEntry> enumeration = zip.getEntries();
            Enumeration<? extends ZipEntry> enumeration = zip.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                File file = new File(this.root + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                    continue;
                }
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                unzip(zip, zipEntry, new File(this.root + zipEntry.getName()));

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (zip != null) {
                zip.close();
            }
        }

    }

    /**
     * Unzip.
     *
     * @param zipFile  the zip file
     * @param zipEntry the zip entry
     * @param saveFile the save file
     * @return void
     * @throws IOException the io exception
     * @Title: unzip
     * @Description:
     */
    public void unzip(ZipFile zipFile, ZipEntry zipEntry, File saveFile)
            throws IOException {
        if (zipEntry == null || zipEntry.isDirectory()) {
            return;
        }

        if (!isCover && saveFile.exists()) {
            saveFile = new File(saveFile.getPath() + "."
                    + System.currentTimeMillis());
        }
        InputStream in = null;
        OutputStream out = null;
        String commentMd5 = null;
        String md5 = null;
        long start = System.currentTimeMillis();
        try {
            commentMd5 = (String) JsonUtils.getValue("md5",
                    zipEntry.getComment());
            in = new BufferedInputStream(zipFile.getInputStream(zipEntry),
                    this.bufferSizipEntry);
            out = new FileOutputStream(saveFile);
            byte[] buffer = new byte[this.bufferSizipEntry];
            int length = -1;
            long count = zipEntry.getSize() / this.bufferSizipEntry + 1;
            long index = 1;
            long read = 0;

            MessageDigest md = DigestUtils.getMd5Digest();
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
                out.flush();
                md.update(buffer, 0, length);

                if (this.progress
                        && ((read = read + length) >= this.bufferSizipEntry
                        * index)) {
                    double d = (index++ * 100.000 / count);
                    double speed = 1.000
                            * read
                            * 1000
                            / (1024 * 1024 * 1.000 * (System
                            .currentTimeMillis() - start));
                    CmdUtils.printMark(DateTimeUtils.getDateTime()
                            + " [INFO] unzip   "
                            // + zipEntry.getName()
                            // + " -> "
                            // + file.getPath()
                            // + " "
                            + String.format("%5.2f", d)
                            + "% "
                            + String.format(
                            "%" + (zipEntry.getSize() + "").length()
                                    + "d", read) + "/"
                            + zipEntry.getSize() + " "
                            + String.format("%.3f", speed) + "m/s ");
                }
            }
            md5 = new String(Hex.encodeHex(md.digest()));
            if (this.progress) {
                CmdUtils.printMark(DateTimeUtils.getDateTime()
                        + " [INFO] zip   "
                        // + zipEntry.getName() + " -> " + file.getPath()
                        + " 100.000% " + read + "/" + zipEntry.getSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        double speed = 1.000 * saveFile.length() * 1000
                / (1024 * 1024 * 1.000 * (System.currentTimeMillis() - start));

        String msg = new StringBuffer()
                .append(commentMd5 == null ? "unzip (Without comment) "
                        : "unzip ")
                .append(zipEntry.getName())
                .append(" -> ")
                .append(saveFile.getPath())
                .append((this.progress ? " " + String.format("%.3f", speed)
                        + "m/s" : "")).append(" md5: ").append(md5).toString();

        if (commentMd5 == null || (md5 != null && md5.equals(commentMd5))) {
            saveFile.setLastModified(zipEntry.getTime());

            if (commentMd5 == null) {
                LogUtils.LOGGER.warn(msg);
            } else {
                LogUtils.LOGGER.info(msg);
            }

            if (!zipEntry.getName().endsWith(".md5")
                    && !zipEntry.getName().endsWith(".javazip.list")) {
                MD5Utils.md5(saveFile);
            }
        } else {
            LogUtils.LOGGER.warn(msg);
        }
    }

    /**
     * Zip.
     *
     * @return void
     * @throws IOException the io exception
     * @Title: zip
     * @Description:
     */
    public void zip() throws IOException {
        File oldZipFile = null;
        if (this.zipFile == null) {
            this.zipFile = new File("NewJavaZip.zip");
        }
        boolean isEmpty = true;
        Iterator<File> iteratorFile = this.zipMap.keySet().iterator();
        while (iteratorFile.hasNext()) {
            if (iteratorFile.next().exists()) {
                isEmpty = false;
                break;
            }
        }
        if (isEmpty && this.version.equals(VERSION_DEF)) {
            return;
        }

        if (this.isAppend && this.zipFile.exists()) {
            oldZipFile = new File(this.zipFile.getPath() + "."
                    + System.currentTimeMillis() + ".tmp.zip");
            new File(this.zipFile.getPath()).renameTo(new File(oldZipFile
                    .getPath()));
            this.zipFile.createNewFile();
        } else {
            this.zipFile.delete();
        }
        if (!this.zipFile.exists()) {
            this.zipFile.createNewFile();
        }
        ZipOutputStream zipOutputStream = null;
        ZipFile zip = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(
                    this.zipFile, true));
            // zipOutputStream.setUseZip64(this.zip64Mode);
            // zipOutputStream.setEncoding(this.encode);
            zipOutputStream.setLevel(this.level);
            // zipOutputStream.setUseLanguageEncodingFlag(true);
            setZipOutputStreamComment(zipOutputStream);
            zip(zipOutputStream);
            if (this.list.size() > 0) {
                zipOutputStream.closeEntry();
            }
            zipOutputStream.closeEntry();
            Set<String> dirNameList = new HashSet<String>();
            Iterator<ZipEntry> it = this.list.keySet().iterator();
            while (it.hasNext()) {
                ZipEntry zipEntry = it.next();
                dirNameList.add(zipEntry.getName());
            }
            if (oldZipFile != null && oldZipFile.exists()) {
                zip = new ZipFile(oldZipFile);
                // Enumeration<ZipEntry> enumeration = zip.getEntries();
                Enumeration<? extends ZipEntry> enumeration = zip.entries();
                while (enumeration.hasMoreElements()) {
                    ZipEntry zipEntry = enumeration.nextElement();
                    if (zipEntry.getName().endsWith(".javazip.list")) {
                        continue;
                    }
                    if (zipEntry.isDirectory()) {
                        if (dirNameList.contains(zipEntry.getName())) {
                            continue;
                        }
                        zipOutputStream.putNextEntry(zipEntry);
                        zipOutputStream.flush();
                        addZipEntry(zipEntry, true);
                    } else {
                        zip(zip, zipOutputStream, zipEntry);
                    }
                }
                zip.close();
                zip = null;
                oldZipFile.delete();
            }
            Enumeration<ZipEntry> enumeration = getZipEntries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                setZipEntryComment(zipEntry);
            }
            zipFilelist(zipOutputStream, new ZipEntry(this.zipFile.getName()
                    + ".file.javazip.list"));

            zipOutputStream.flush();
            zipOutputStream.finish();
        } catch (IOException e) {
            e.printStackTrace();
            if (zip != null) {
                zip.close();
                zip = null;
            }
            if (zipOutputStream != null) {
                zipOutputStream.close();
                zipOutputStream = null;
            }
            if (oldZipFile != null && oldZipFile.exists()) {

                if (this.zipFile.exists()) {
                    this.zipFile.delete();
                }
                oldZipFile.renameTo(this.zipFile);
            }
            throw e;
        } finally {
            if (zip != null) {
                zip.close();
                zip = null;
            }
            if (zipOutputStream != null) {
                zipOutputStream.close();
                zipOutputStream = null;
            }
        }

    }

    /**
     * Zip.
     *
     * @param zip             the zip
     * @param zipOutputStream the zip output stream
     * @param zipEntry        the zip entry
     * @return void
     * @throws IOException the io exception
     * @Title: zip
     * @Description:
     */
    public void zip(ZipFile zip, ZipOutputStream zipOutputStream,
                    ZipEntry zipEntry) throws IOException {
        if (zipEntry == null || zipEntry.isDirectory()) {
            return;
        }

        Iterator<ZipEntry> it = this.list.keySet().iterator();
        while (it.hasNext()) {
            ZipEntry entry = it.next();
            if (entry.isDirectory()) {
                continue;
            }
            String md5 = (String) JsonUtils.getValue("md5", entry.getComment());
            if (entry.getName().equals(zipEntry.getName())
                    && md5 != null
                    && md5.equals((String) JsonUtils.getValue("md5",
                    zipEntry.getComment()))) {
                return;
            }
        }

        InputStream in = null;
        String comment = null;
        String md5 = null;
        long start = System.currentTimeMillis();
        try {

            if (zipEntry.getComment() != null) {
                comment = (String) JsonUtils.getValue("md5",
                        zipEntry.getComment());
            }
            in = new BufferedInputStream(zip.getInputStream(zipEntry),
                    this.bufferSizipEntry);
            byte[] buffer = new byte[this.bufferSizipEntry];
            int length = -1;
            long count = zipEntry.getSize() / this.bufferSizipEntry + 1;
            long index = 1;
            long read = 0;
            MessageDigest md = DigestUtils.getMd5Digest();
            zipOutputStream.putNextEntry(zipEntry);

            while ((length = in.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, length);
                md.update(buffer, 0, length);
                zipOutputStream.flush();

                if (this.progress
                        && ((read = read + length) >= this.bufferSizipEntry
                        * index)) {
                    double d = (read * 100.000 / zipEntry.getSize());
                    double speed = 1.000
                            * read
                            * 1000
                            / (1024 * 1024 * 1.000 * (System
                            .currentTimeMillis() - start));
                    CmdUtils.printMark(DateTimeUtils.getDateTime()
                            + " [INFO] zip   Append "
                            // + zipEntry.getName()
                            // + " "
                            + String.format("%5.2f", d)
                            + "% "
                            + String.format(
                            "%" + (zipEntry.getSize() + "").length()
                                    + "d", read) + "/"
                            + zipEntry.getSize() + " "
                            + String.format("%.3f", speed) + "m/s ");
                }
            }
            md5 = new String(Hex.encodeHex(md.digest()));
            if (this.progress) {
                CmdUtils.printMark(DateTimeUtils.getDateTime()
                        + " [INFO] zip   Append "
                        // + zipEntry.getName()
                        + " 100.000% " + read + "/" + zipEntry.getSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(in);
        }

        double speed = 1.000 * zipEntry.getSize() * 1000
                / (1024 * 1024 * 1.000 * (System.currentTimeMillis() - start));

        String msg = new StringBuffer()
                .append("zip   Append ")
                .append(zipEntry.getName())
                .append((this.progress ? " " + String.format("%.3f", speed)
                        + "m/s" : "")).append(" md5: ").append(md5).toString();
        if (comment == null) {
            String json = JsonUtils.setValue("Append",
                    this.zipFile.getAbsolutePath(), zipEntry.getComment());
            zipEntry.setComment(json);
            setZipEntryComment(zipEntry, md5);
            addZipEntry(zipEntry, true);
            LogUtils.LOGGER.info(msg);
            return;
        }

        if (md5 != null && md5.equals(comment)) {
            String json = JsonUtils.setValue("Append",
                    this.zipFile.getAbsolutePath(), zipEntry.getComment());
            zipEntry.setComment(json);
            addZipEntry(zipEntry, true);
            LogUtils.LOGGER.info(msg);
        } else {
            LogUtils.LOGGER.warn(msg);
        }
        setZipEntryComment(zipEntry, "speed", String.format("%.3f", speed) + "");
    }

    /**
     * Zip.
     *
     * @param zipOutputStream the zip output stream
     * @return void
     * @throws IOException the io exception
     * @Title: zip
     * @Description:
     */
    public void zip(ZipOutputStream zipOutputStream) throws IOException {
        if (this.root != null && !"".equals(this.root)) {
            ZipEntry rootZipEntry = new ZipEntry(this.root);
            setZipEntryComment(rootZipEntry);
            addZipEntry(rootZipEntry, true);
            zipOutputStream.putNextEntry(rootZipEntry);
            zipOutputStream.flush();
        }
        Iterator<File> it = this.zipMap.keySet().iterator();
        while (it.hasNext()) {
            File file = it.next();
            if (!file.exists()) {
                // System.out.println(file + " Not exist.");
                LogUtils.LOGGER.info(" zip   Not exist:"
                        + file.getAbsolutePath());
                continue;
            }
            String path = this.zipMap.get(file);
            if (file.isDirectory()) {
                String dir = (path == null ? "" : path) + file.getName() + "/";
                ZipEntry zipEntryDir = new ZipEntry(this.root + dir);
                zipEntryDir.setTime(file.lastModified());
                setZipEntryComment(zipEntryDir, file);
                zipEntryDir.setComment(JsonUtils.setValue("zipEntryPath", path,
                        zipEntryDir.getComment()));
                addZipEntry(zipEntryDir, true);
                zipOutputStream.putNextEntry(zipEntryDir);
                zipOutputStream.flush();
                zip(zipOutputStream, file, dir);
                continue;
            }

            ZipEntry zipEntry = new ZipEntry(this.root
                    + (path == null ? "" : path) + file.getName());
            zipEntry.setTime(file.lastModified());
            zip(zipOutputStream, file, zipEntry);
            setZipEntryComment(zipEntry, file);
            zipEntry.setComment(JsonUtils.setValue("zipEntryPath", path,
                    zipEntry.getComment()));

        }

    }

    /**
     * Zip.
     *
     * @param zipOutputStream the zip output stream
     * @param dir             the dir
     * @param path            the path
     * @return void
     * @throws IOException the io exception
     * @Title: zip
     * @Description:
     */
    public void zip(ZipOutputStream zipOutputStream, File dir, String path)
            throws IOException {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (!file.exists()) {
                continue;
            }
            if (file.isDirectory()) {
                String dirZipEntry = (path == null ? "" : path)
                        + file.getName() + "/";
                ZipEntry zipEntryDir = new ZipEntry(this.root + dirZipEntry);
                zipEntryDir.setTime(file.lastModified());
                setZipEntryComment(zipEntryDir, file);
                zipEntryDir.setComment(JsonUtils.setValue("zipEntryPath", path,
                        zipEntryDir.getComment()));
                addZipEntry(zipEntryDir, true);
                zipOutputStream.putNextEntry(zipEntryDir);
                zipOutputStream.flush();
                zip(zipOutputStream, file, dirZipEntry);
                continue;
            }
            ZipEntry zipEntry = new ZipEntry(this.root
                    + (path == null ? "" : path) + file.getName());
            zipEntry.setTime(file.lastModified());
            zip(zipOutputStream, file, zipEntry);
            setZipEntryComment(zipEntry, file);
        }
    }

    /**
     * Zip.
     *
     * @param zipOutputStream the zip output stream
     * @param file            the file
     * @param zipEntry        the zip entry
     * @return void
     * @throws IOException the io exception
     * @Title: zip
     * @Description:
     */
    public void zip(ZipOutputStream zipOutputStream, File file,
                    ZipEntry zipEntry) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException(file + "(Not exist)");
        }

        // System.out.println("zip " + zipEntry.getName() + " <- " +
        // file.getPath());
        InputStream in = null;
        String md5 = null;
        long start = System.currentTimeMillis();
        try {
            in = new BufferedInputStream(new FileInputStream(file),
                    this.bufferSizipEntry);
            byte[] buffer = new byte[this.bufferSizipEntry];
            int length = -1;
            MessageDigest md = DigestUtils.getMd5Digest();
            zipOutputStream.putNextEntry(zipEntry);
            long index = 1;
            long read = 0;
            while ((length = in.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, length);
                md.update(buffer, 0, length);
                zipOutputStream.flush();
                if (this.progress
                        && ((read = read + length) >= this.bufferSizipEntry
                        * index)) {
                    double d = (read * 100.000 / file.length());
                    double speed = 1.000
                            * read
                            * 1000
                            / (1024 * 1024 * 1.000 * (System
                            .currentTimeMillis() - start));
                    CmdUtils.printMark(DateTimeUtils.getDateTime()
                            + " [INFO] zip   "
                            // + zipEntry.getName()
                            // + " <- "
                            // + file.getPath()
                            // + " "
                            + String.format("%5.2f", d)
                            + "% "
                            + String.format("%" + (file.length() + "").length()
                            + "d", read) + "/" + file.length() + " "
                            + String.format("%.3f", speed) + "m/s ");
                }
            }
            md5 = new String(Hex.encodeHex(md.digest()));
            if (this.progress) {
                CmdUtils.printMark(DateTimeUtils.getDateTime()
                        + " [INFO] zip   "
                        // + zipEntry.getName() + " <- " + file.getPath()
                        + " 100.000% " + +read + "/" + file.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(in);
        }
        double speed = 1.000 * file.length() * 1000
                / (1024 * 1024 * 1.000 * (System.currentTimeMillis() - start));

        String msg = new StringBuffer()
                .append("zip   ")
                .append(zipEntry.getName())
                .append(" <- ")
                .append(file.getPath())
                .append((this.progress ? " " + String.format("%.3f", speed)
                        + "m/s" : "")).append(" md5: ").append(md5).toString();

        if (md5 != null && md5.length() > 1) {
            setZipEntryComment(zipEntry, file, md5);
            addZipEntry(zipEntry, true);
            LogUtils.LOGGER.info(msg);
        } else {
            addZipEntry(zipEntry, false);
            LogUtils.LOGGER.info(msg);
        }
        setZipEntryComment(zipEntry, "speed", String.format("%.3f", speed) + "");
    }

    /**
     * @param zipEntry
     * @return void
     * @throws
     * @Title: setZipEntryComment
     * @Description:
     */
    private void setZipEntryComment(ZipEntry zipEntry) {
        if (zipEntry == null) {
            return;
        }
        String json = JsonUtils.setValue("type", zipEntry.isDirectory() ? "D"
                : "F", zipEntry.getComment());
        json = JsonUtils.setValue("name", zipEntry.getName(), json);
        json = JsonUtils.setValue("compressedSizipEntry",
                zipEntry.getCompressedSize() + "", json);
        json = JsonUtils.setValue("CRC", zipEntry.getCrc() + "", json);
        zipEntry.setComment(json);
    }

    /**
     * @param zipEntry
     * @param file
     * @return void
     * @throws
     * @Title: setZipEntryComment
     * @Description:
     */
    private void setZipEntryComment(ZipEntry zipEntry, File file) {

        setZipEntryComment(zipEntry, file, null);
    }

    /**
     * @param zipEntry
     * @param file
     * @param md5
     * @return void
     * @throws
     * @Title: setZipEntryComment
     * @Description:
     */
    private void setZipEntryComment(ZipEntry zipEntry, File file, String md5) {
        if (zipEntry == null) {
            return;
        }
        String json = JsonUtils.setValue("name", zipEntry.getName(),
                zipEntry.getComment());
        json = JsonUtils.setValue("type", file.isDirectory() ? "D" : "F", json);
        json = JsonUtils.setValue("from", file.getAbsolutePath(), json);
        json = JsonUtils.setValue("version", this.version, json);
        if (md5 != null && !file.isDirectory()) {
            json = JsonUtils.setValue("md5", md5, json);
        }

        if (!file.isDirectory()) {
            try {
                json = JsonUtils.setValue("md5Start1k",
                        MD5Utils.getFileMd5(file, 0, 1024L), json);
                json = JsonUtils.setValue("md5End1k",
                        MD5Utils.getFileMd5(file, file.length() - 1024, 1024L),
                        json);
            } catch (IOException e) {
            }
        }
        json = JsonUtils.setValue("CRC", zipEntry.getCrc() + "", json);

        json = JsonUtils.setValue("lastModified", file.lastModified() + "",
                json);
        json = JsonUtils.setValue("lastModifiedTime",
                DateTimeUtils.getDateTime(file.lastModified()), json);
        json = JsonUtils.setValue("CRC", zipEntry.getCrc() + "", json);
        json = JsonUtils.setValue("length", file.length() + "", json);
        json = JsonUtils.setValue("compressedSizipEntry",
                zipEntry.getCompressedSize() + "", json);

        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            json = JsonUtils.setValue("hostname", addr.getHostName(), json);
            json = JsonUtils.setValue("ip", addr.getHostAddress().toString(),
                    json);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        json = JsonUtils.setValue("os", System.getProperty("os.name"), json);
        json = JsonUtils.setValue("encode", this.encode, json);
        json = JsonUtils.setValue("root", this.root == null ? "/" : this.root,
                json);
        zipEntry.setComment(json);
    }

    /**
     * @param zipEntry
     * @param md5
     * @return void
     * @throws
     * @Title: setZipEntryComment
     * @Description:
     */
    private void setZipEntryComment(ZipEntry zipEntry, String md5) {
        if (zipEntry == null) {
            return;
        }
        String json = JsonUtils.setValue("name", zipEntry.getName(),
                zipEntry.getComment());
        json = JsonUtils.setValue("type", zipEntry.isDirectory() ? "D" : "F",
                json);
        json = JsonUtils.setValue("from", null, json);
        if (md5 != null) {
            json = JsonUtils.setValue("md5", md5, json);
        }
        json = JsonUtils.setValue("CRC", zipEntry.getCrc() + "", json);

        json = JsonUtils
                .setValue("lastModified", zipEntry.getTime() + "", json);
        json = JsonUtils.setValue("lastModifiedTime",
                DateTimeUtils.getDateTime(zipEntry.getTime()), json);
        json = JsonUtils.setValue("CRC", zipEntry.getCrc() + "", json);
        json = JsonUtils.setValue("length", zipEntry.getSize() + "", json);
        json = JsonUtils.setValue("compressedSizipEntry",
                zipEntry.getCompressedSize() + "", json);

        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            json = JsonUtils.setValue("hostname", addr.getHostName(), json);
            json = JsonUtils.setValue("ip", addr.getHostAddress().toString(),
                    json);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        json = JsonUtils.setValue("os", System.getProperty("os.name"), json);
        json = JsonUtils.setValue("encode", this.encode, json);
        json = JsonUtils.setValue("root", this.root == null ? "/" : this.root,
                json);
        zipEntry.setComment(json);
    }

    // public Zip64Mode getZip64Mode() {
    // return this.zip64Mode;
    // }

    // public void setZip64Mode(Zip64Mode zip64Mode) {
    // this.zip64Mode = zip64Mode;
    // }

    /**
     * @param zipEntry
     * @param key
     * @param value
     * @return void
     * @throws
     * @Title: setZipEntryComment
     * @Description:
     */
    private void setZipEntryComment(ZipEntry zipEntry, String key, String value) {
        zipEntry.setComment(JsonUtils.setValue(key, value,
                zipEntry.getComment()));
    }

    /**
     * @param zipOutputStream
     * @return void
     * @throws
     * @Title: setZipOutputStreamComment
     * @Description:
     */
    private void setZipOutputStreamComment(ZipOutputStream zipOutputStream) {
        String json = JsonUtils.setValue("os", System.getProperty("os.name"),
                "");
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            json = JsonUtils.setValue("hostname", addr.getHostName(), json);
            json = JsonUtils.setValue("ip", addr.getHostAddress().toString(),
                    json);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        json = JsonUtils.setValue("level", this.level + "", json);
        json = JsonUtils.setValue("version", this.version + "", json);
        // json = JsonUtils
        // .setValue("encode", zipOutputStream.getEncoding(), json);
        zipOutputStream.setComment(json);
    }

    /**
     * @param zipOutputStream
     * @param zipEntry
     * @return void
     * @throws IOException
     * @throws
     * @Title: zipFilelist
     * @Description:
     */
    private void zipFilelist(ZipOutputStream zipOutputStream, ZipEntry zipEntry)
            throws IOException {
        // ZipEntry zipEntry = new ZipEntry(this.zipFile.getName() + ".list");
        zipEntry.setTime(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        Enumeration<ZipEntry> enumeration = getZipEntries();
        while (enumeration.hasMoreElements()) {
            sb.append(enumeration.nextElement().getComment()).append("\n");
        }
        if (sb.length() == 0) {
            sb.append("");
        }
        zipString(zipOutputStream, zipEntry, sb.toString());
    }

    /**
     * @param zipOutputStream
     * @param zipEntry
     * @param str
     * @return void
     * @throws IOException
     * @throws
     * @Title: zipString
     * @Description:
     */
    private void zipString(ZipOutputStream zipOutputStream, ZipEntry zipEntry,
                           String str) throws IOException {

        // ZipEntry zipEntry = new ZipEntry(this.zipFile.getName() + ".list");
        zipEntry.setTime(System.currentTimeMillis());
        zipEntry.setComment(JsonUtils.setValue("name", zipEntry.getName(),
                zipEntry.getComment()));
        zipEntry.setComment(JsonUtils.setValue("type", "TXT",
                zipEntry.getComment()));
        zipEntry.setComment(JsonUtils.setValue("type", "TXT",
                zipEntry.getComment()));
        zipEntry.setComment(JsonUtils.setValue("lastModified",
                zipEntry.getTime() + "", zipEntry.getComment()));
        zipEntry.setComment(JsonUtils.setValue("lastModifiedTime",
                DateTimeUtils.getDateTime(zipEntry.getTime()),
                zipEntry.getComment()));

        zipOutputStream.putNextEntry(zipEntry);

        try {
            byte[] buffer = str.getBytes();
            zipOutputStream.write(buffer, 0, buffer.length);
            zipOutputStream.flush();
            zipEntry.setComment(JsonUtils.setValue("md5",
                    DigestUtils.md5Hex(buffer), zipEntry.getComment()));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {

        }
    }

}
