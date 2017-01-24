package com.forsrc.utils;


import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The type Star dict utils.
 */
public class StarDictUtils {

    /**
     * The constant UTF8_END_BYTE.
     */
    public static byte UTF8_END_BYTE = 0x00;
    /**
     * The constant CHARSET_NAME.
     */
    public static String CHARSET_NAME = "UTF-8";

    /**
     * Byte array to int int.
     *
     * @param bytesToConvert the bytes to convert
     * @return the int
     */
    public static int byteArrayToInt(final byte[] bytesToConvert) {
        byte[] bytes = bytesToConvert;
        if (bytesToConvert.length < 4) {
            bytes = new byte[4];
            System.arraycopy(bytesToConvert, 0, bytes, 0, bytesToConvert.length);
        }
        return (bytes[0] & 0xff) << 24 |
                (bytes[1] & 0xff) << 16 |
                (bytes[2] & 0xff) << 8 |
                (bytes[3] & 0xff);
    }

    /**
     * Filter utf 8 mb 4 string.
     *
     * @param text the text
     * @return the string
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String filterUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes(CHARSET_NAME);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
            }
        }
        buffer.flip();
        return new String(buffer.array(), CHARSET_NAME);
    }

    /**
     * Gets info.
     *
     * @param file the file
     * @return the info
     * @throws FileNotFoundException the file not found exception
     */
    public static Info getInfo(File file) throws FileNotFoundException {
        Info info = new Info();
        Scanner scanner = new Scanner(file, CHARSET_NAME);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ((line).indexOf('=') != -1) {
                String[] pair = line.split("=", 2);
                info.getInfo().put(pair[0], pair[1]);
            }
        }
        return info;
    }

    /**
     * Get file bytes byte [ ].
     *
     * @param file the file
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] getFileBytes(File file) throws IOException {
        byte[] fileBytes;
        FileInputStream fileIn = new FileInputStream(file);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[524288];   // 512KB
        int bytesCount;
        while (fileIn.available() != 0) {
            bytesCount = fileIn.read(buffer);
            outStream.write(buffer, 0, bytesCount);
        }
        fileBytes = outStream.toByteArray();
        fileIn.close();
        outStream.close();
        return fileBytes;
    }

    /**
     * Gets syn.
     *
     * @param currentIndex the current index
     * @param fileBytes    the file bytes
     * @return the syn
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static Syn getSyn(int currentIndex, byte[] fileBytes) throws UnsupportedEncodingException {
        if (currentIndex >= fileBytes.length) {
            return null;
        }
        Syn syn = new Syn();
        syn.setCurrentIndex(currentIndex);
        int baseIndex = currentIndex;
        while (fileBytes[currentIndex] != UTF8_END_BYTE) {
            currentIndex++;
            if (currentIndex >= fileBytes.length) {
                syn.setNextIndex(fileBytes.length);
                return syn;
            }
        }
        int wordBytesCount = currentIndex - baseIndex;
        String word = new String(fileBytes, baseIndex, wordBytesCount, CHARSET_NAME);
        syn.setWord(word);
        currentIndex++;
        byte[] synIndexBytes = new byte[4];
        System.arraycopy(fileBytes, currentIndex, synIndexBytes, 0, 4);
        int synIndex = byteArrayToInt(synIndexBytes);
        currentIndex += 4;
        syn.setNextIndex(currentIndex);
        return syn;
    }

    /**
     * Gets idx.
     *
     * @param currentIndex the current index
     * @param fileBytes    the file bytes
     * @return the idx
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static Idx getIdx(int currentIndex, byte[] fileBytes) throws UnsupportedEncodingException {
        if (currentIndex >= fileBytes.length) {
            return null;
        }
        Idx idx = new Idx();
        int baseIndex = currentIndex;
        idx.setCurrentIndex(currentIndex);

        while (fileBytes[currentIndex] != UTF8_END_BYTE) {
            currentIndex++;
            if (currentIndex >= fileBytes.length) {
                idx.setNextIndex(fileBytes.length);
                return idx;
            }
        }

        int wordBytesCount = currentIndex - baseIndex;
        String word = new String(fileBytes, baseIndex, wordBytesCount, CHARSET_NAME);
        idx.setWord(word);
        currentIndex++;
        byte[] dataOffsetBytes = new byte[4];
        System.arraycopy(fileBytes, currentIndex, dataOffsetBytes, 0, 4);
        int dataOffset = byteArrayToInt(dataOffsetBytes);
        idx.setDataOffset(dataOffset);
        currentIndex += 4;
        byte[] dataSizeBytes = new byte[4];
        System.arraycopy(fileBytes, currentIndex, dataSizeBytes, 0, 4);
        int dataSize = byteArrayToInt(dataSizeBytes);
        idx.setDataSize(dataSize);
        currentIndex += 4;
        idx.setNextIndex(currentIndex);
        return idx;
    }

    /**
     * Handle.
     *
     * @param fileName the file name
     * @param handler  the handler
     * @throws Exception the exception
     */
    public static void handle(String fileName, Handler handler) throws Exception {
        File idxFile = new File(fileName + ".idx");
        File dictFile = new File(fileName + ".dict");
        File ifoFile = new File(fileName + ".ifo");
        File synFile = new File(fileName + ".syn");
        Info info = getInfo(ifoFile);
        int currentIndex = 0;
        Idx idx = null;
        Syn syn = null;
        byte[] idxFileBytes = getFileBytes(idxFile);
        byte[] dictFileBytes = getFileBytes(dictFile);
        byte[] synFileBytes = synFile.exists() ? getFileBytes(synFile) : new byte[0];
        int index = 0;
        idx = getIdx(currentIndex, idxFileBytes);
        if (idx == null) {
            return;
        }
        do {
            String definition = getDefinition(dictFileBytes, idx.getDataOffset(), idx.getDataSize());
            syn = getSyn(currentIndex, synFileBytes);
            currentIndex = idx.getNextIndex();
            Idx next = getIdx(currentIndex, idxFileBytes);
            if (!handler.handle(index++, next != null, info, idx, syn, definition)) {
                break;
            }
            idx = next;
        } while (idx != null);
    }

    /**
     * Gets definition.
     *
     * @param fileBytes the file bytes
     * @param offset    the offset
     * @param size      the size
     * @return the definition
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public static String getDefinition(byte[] fileBytes, int offset, int size) throws UnsupportedEncodingException {
        if (offset >= fileBytes.length) {
            return "";
        }
        size = offset + size > fileBytes.length ? fileBytes.length - offset : size;
        String definition = new String(fileBytes, offset, size, CHARSET_NAME);
        return definition;
    }

    /**
     * The interface Handler.
     */
    public static interface Handler {
        /**
         * Handle boolean.
         *
         * @param index      the index
         * @param hasNext    the has next
         * @param info       the info
         * @param idx        the idx
         * @param syn        the syn
         * @param definition the definition
         * @return the boolean
         * @throws Exception the exception
         */
        public boolean handle(int index, boolean hasNext, Info info, Idx idx, Syn syn, String definition) throws Exception;
    }

    /**
     * The type Info.
     */
    public static class Info {

        private Map<String, String> info = new HashMap<String, String>();

        /**
         * Gets version.
         *
         * @return the version
         */
        public String getVersion() {
            return info.get("version");
        }

        /**
         * Gets date.
         *
         * @return the date
         */
        public String getDate() {
            return info.get("date");
        }

        /**
         * Gets description.
         *
         * @return the description
         */
        public String getDescription() {
            return info.get("description");
        }

        /**
         * Gets same type sequence.
         *
         * @return the same type sequence
         */
        public String getSameTypeSequence() {
            return info.get("sametypesequence");
        }

        /**
         * Gets idx file size.
         *
         * @return the idx file size
         */
        public String getIdxFileSize() {
            return info.get("idxfilesize");
        }

        /**
         * Gets bookname.
         *
         * @return the bookname
         */
        public String getBookname() {
            return info.get("bookname");
        }

        /**
         * Gets author.
         *
         * @return the author
         */
        public String getAuthor() {
            return info.get("author");
        }

        /**
         * Gets wordcount.
         *
         * @return the wordcount
         */
        public String getWordcount() {
            return info.get("wordcount");
        }

        /**
         * Gets synwordcount.
         *
         * @return the synwordcount
         */
        public String getSynwordcount() {
            return info.get("synwordcount");
        }

        /**
         * Gets info.
         *
         * @return the info
         */
        public Map<String, String> getInfo() {
            return this.info;
        }

        /**
         * Sets info.
         *
         * @param info the info
         */
        public void setInfo(Map<String, String> info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "info=" + info +
                    '}';
        }
    }

    /**
     * The type Idx.
     */
    public static class Idx {

        private String word;
        private int dataOffset;
        private int dataSize;
        private int nextIndex;
        private int currentIndex;

        /**
         * Gets next index.
         *
         * @return the next index
         */
        public int getNextIndex() {
            return this.nextIndex;
        }

        /**
         * Sets next index.
         *
         * @param nextIndex the next index
         */
        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }

        /**
         * Gets word.
         *
         * @return the word
         */
        public String getWord() {
            return this.word;
        }

        /**
         * Sets word.
         *
         * @param word the word
         */
        public void setWord(String word) {
            this.word = word;
        }

        /**
         * Gets data offset.
         *
         * @return the data offset
         */
        public int getDataOffset() {
            return this.dataOffset;
        }

        /**
         * Sets data offset.
         *
         * @param dataOffset the data offset
         */
        public void setDataOffset(int dataOffset) {
            this.dataOffset = dataOffset;
        }

        /**
         * Gets data size.
         *
         * @return the data size
         */
        public int getDataSize() {
            return this.dataSize;
        }

        /**
         * Sets data size.
         *
         * @param dataSize the data size
         */
        public void setDataSize(int dataSize) {
            this.dataSize = dataSize;
        }

        /**
         * Gets current index.
         *
         * @return the current index
         */
        public int getCurrentIndex() {
            return this.currentIndex;
        }

        /**
         * Sets current index.
         *
         * @param currentIndex the current index
         */
        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }
    }

    /**
     * The type Syn.
     */
    public static class Syn {
        private String word;
        private int currentIndex;
        private int synIndex;
        private int nextIndex;

        /**
         * Gets word.
         *
         * @return the word
         */
        public String getWord() {
            return this.word;
        }

        /**
         * Sets word.
         *
         * @param word the word
         */
        public void setWord(String word) {
            this.word = word;
        }

        /**
         * Gets current index.
         *
         * @return the current index
         */
        public int getCurrentIndex() {
            return this.currentIndex;
        }

        /**
         * Sets current index.
         *
         * @param currentIndex the current index
         */
        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        /**
         * Gets syn index.
         *
         * @return the syn index
         */
        public int getSynIndex() {
            return this.synIndex;
        }

        /**
         * Sets syn index.
         *
         * @param synIndex the syn index
         */
        public void setSynIndex(int synIndex) {
            this.synIndex = synIndex;
        }

        /**
         * Gets next index.
         *
         * @return the next index
         */
        public int getNextIndex() {
            return this.nextIndex;
        }

        /**
         * Sets next index.
         *
         * @param nextIndex the next index
         */
        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }
    }


}
