package com.forsrc.tools;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type My lucene utils.
 */
public class MyLuceneUtils {

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();

    private static ThreadLocal<String> _indexDir = new ThreadLocal<String>();
    private static ThreadLocal<String> _simpleHTMLFormatterPreTag = new ThreadLocal<String>();
    private static ThreadLocal<String> _simpleHTMLFormatterPostTag = new ThreadLocal<String>();
    private static ThreadLocal<Analyzer> _analyzer = new ThreadLocal<Analyzer>();

    private static ThreadLocal<IndexWriter> _indexWriter = new ThreadLocal<IndexWriter>();

    private MyLuceneUtils() {
    }

    /**
     * Create my lucene utils.
     *
     * @return the my lucene utils
     */
    public static MyLuceneUtils create() {
        return MyLuceneUtilsStaticClass.INSTANCE;
    }

    /**
     * Create my lucene utils.
     *
     * @param indexWriter the index writer
     * @return the my lucene utils
     */
    public static MyLuceneUtils create(IndexWriter indexWriter) {
        MyLuceneUtils myLuceneUtils = MyLuceneUtilsStaticClass.INSTANCE;
        myLuceneUtils.setIndexWriter(indexWriter);
        myLuceneUtils.setAnalyzer(indexWriter.getAnalyzer());
        return myLuceneUtils;
    }

    /**
     * Create my lucene utils.
     *
     * @param indexWriter the index writer
     * @param analyzer    the analyzer
     * @return the my lucene utils
     */
    public static MyLuceneUtils create(IndexWriter indexWriter, Analyzer analyzer) {
        MyLuceneUtils myLuceneUtils = MyLuceneUtilsStaticClass.INSTANCE;
        myLuceneUtils.setIndexWriter(indexWriter);
        myLuceneUtils.setAnalyzer(analyzer);
        return myLuceneUtils;
    }

    /**
     * Create my lucene utils.
     *
     * @param indexDir the index dir
     * @param analyzer the analyzer
     * @return the my lucene utils
     */
    public static MyLuceneUtils create(String indexDir, Analyzer analyzer) throws MyLuceneUtilsException {
        MyLuceneUtils myLuceneUtils = MyLuceneUtilsStaticClass.INSTANCE;
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = null;
        try {
            indexWriter = myLuceneUtils.getIndexWriter(new SimpleFSDirectory(Paths.get(indexDir)), indexWriterConfig);
        } catch (MyLuceneUtilsException e) {
            throw e;
        } catch (IOException e) {
            throw new MyLuceneUtilsException(e);
        }
        myLuceneUtils.setIndexWriter(indexWriter);
        myLuceneUtils.setAnalyzer(analyzer);
        return myLuceneUtils;
    }

    /**
     * Gets index dir.
     *
     * @return the index dir
     */
    public static String getIndexDir() {
        return _indexDir.get();
    }

    /**
     * Sets index dir.
     *
     * @param indexDir the index dir
     * @return the index dir
     */
    public MyLuceneUtils setIndexDir(String indexDir) {
        _indexDir.set(indexDir);
        return this;
    }

    /**
     * Close my lucene utils.
     *
     * @return the my lucene utils
     * @throws IOException the io exception
     */
    public MyLuceneUtils close() throws IOException {
        _simpleHTMLFormatterPostTag.remove();
        _simpleHTMLFormatterPostTag.remove();
        Analyzer analyzer = _analyzer.get();
        if (analyzer != null) {
            analyzer.close();
            _analyzer.remove();
        }

        IndexWriter indexWriter = _indexWriter.get();
        if (indexWriter != null) {
            try {
                indexWriter.close();
                indexWriter.getDirectory().close();
            } catch (IOException e) {
                throw e;
            } finally {
                indexWriter = null;
                _indexWriter.remove();
            }
        }
        return this;
    }

    /**
     * Gets simple html formatter pre tag.
     *
     * @return the simple html formatter pre tag
     */
    public String getSimpleHTMLFormatterPreTag() {
        return _simpleHTMLFormatterPreTag.get();
    }

    /**
     * Sets simple html formatter pre tag.
     *
     * @param simpleHTMLFormatterPreTag the simple html formatter pre tag
     * @return the simple html formatter pre tag
     */
    public MyLuceneUtils setSimpleHTMLFormatterPreTag(String simpleHTMLFormatterPreTag) {
        _simpleHTMLFormatterPreTag.set(simpleHTMLFormatterPreTag);
        return this;
    }

    /**
     * Gets simple html formatter post tag.
     *
     * @return the simple html formatter post tag
     */
    public String getSimpleHTMLFormatterPostTag() {
        return _simpleHTMLFormatterPostTag.get();
    }

    /**
     * Sets simple html formatter post tag.
     *
     * @param simpleHTMLFormatterPostTag the simple html formatter post tag
     * @return the simple html formatter post tag
     */
    public MyLuceneUtils setSimpleHTMLFormatterPostTag(String simpleHTMLFormatterPostTag) {
        _simpleHTMLFormatterPostTag.set(simpleHTMLFormatterPostTag);
        return this;
    }

    /**
     * Gets analyzer.
     *
     * @return the analyzer
     */
    public Analyzer getAnalyzer() {
        return _analyzer.get();
    }

    /**
     * Sets analyzer.
     *
     * @param analyzer the analyzer
     * @return the analyzer
     */
    public MyLuceneUtils setAnalyzer(Analyzer analyzer) {
        _analyzer.set(analyzer);
        return this;
    }

    /**
     * Gets index writer.
     *
     * @return the index writer
     */
    public IndexWriter getIndexWriter() {
        return _indexWriter.get();
    }

    /**
     * Sets index writer.
     *
     * @param indexWriter the index writer
     * @return the index writer
     */
    public MyLuceneUtils setIndexWriter(IndexWriter indexWriter) {
        _indexWriter.set(indexWriter);
        return this;
    }

    /**
     * Gets index writer.
     *
     * @param dir    the dir
     * @param config the config
     * @return the index writer
     */
    public IndexWriter getIndexWriter(Directory dir, IndexWriterConfig config) throws MyLuceneUtilsException {
        if (null == dir) {
            throw new IllegalArgumentException("Directory can not be null.");
        }
        if (null == config) {
            throw new IllegalArgumentException("IndexWriterConfig can not be null.");
        }
        IndexWriter indexWriter = null;

        try {
            WRITE_LOCK.lock();
            if (IndexWriter.isLocked(dir)) {
                throw new LockObtainFailedException("Directory of index had been locked.");
            }
            indexWriter = new IndexWriter(dir, config);

        } catch (LockObtainFailedException e) {
            throw new MyLuceneUtilsException(e);
        } catch (IOException e) {
            throw new MyLuceneUtilsException(e);
        } finally {
            WRITE_LOCK.unlock();
        }
        return indexWriter;
    }

    public synchronized void index(Callback<IndexWriter> callback) throws MyLuceneUtilsException {

        WRITE_LOCK.lock();
        try {

            IndexWriter indexWriter = _indexWriter.get();
            callback.handle(indexWriter);
            indexWriter.commit();

        } catch (IOException e) {
            throw new MyLuceneUtilsException(e);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * Index.
     *
     * @param list the list
     * @throws Exception the exception
     */
    public void index(final List<Map<String, String>> list) throws Exception {

        index(new Callback<IndexWriter>() {
            @Override
            public void handle(final IndexWriter indexWriter) throws MyLuceneUtilsException {
                try {
                    for (Map<String, String> map : list) {
                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                        Document doc = new Document();
                        while (it.hasNext()) {
                            Map.Entry<String, String> entry = it.next();
                            Field field = new Field(entry.getKey(), entry.getValue(), TextField.TYPE_STORED);
                            doc.add(field);
                        }
                        indexWriter.addDocument(doc);
                    }
                } catch (IOException e) {
                    throw new MyLuceneUtilsException(e);
                }
            }
        });
    }


    public interface Callback<T> {
        void handle(final T t) throws MyLuceneUtilsException;
    }

    private static class MyLuceneUtilsStaticClass {
        private static final MyLuceneUtils INSTANCE = new MyLuceneUtils();
    }

    /**
     * The type My lucene utils exception.
     */
    public static class MyLuceneUtilsException extends Exception {
        /**
         * Instantiates a new My lucene utils exception.
         *
         * @param msg the msg
         */
        public MyLuceneUtilsException(String msg) {
            super(msg);
        }

        /**
         * Instantiates a new My lucene utils exception.
         *
         * @param e the e
         */
        public MyLuceneUtilsException(Exception e) {
            super(e);
        }

    }

}
