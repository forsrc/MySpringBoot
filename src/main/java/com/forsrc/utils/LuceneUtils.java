package com.forsrc.utils;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.*;

/**
 * The type Lucene utils.
 */
public class LuceneUtils {
    /**
     * The constant FONT_START_TAG.
     */
    public static final String FONT_START_TAG = "<font color='red'>";
    /**
     * The constant FONT_END_TAG.
     */
    public static final String FONT_END_TAG = "</font>";
    /**
     * The constant DEF_DIR.
     */
    public static final String DEF_DIR = "/index";
    /**
     * The constant VERSION.
     */
    public static final Version VERSION = Version.LATEST;
    private IndexWriter indexWriter;
    private Analyzer analyzer;
    private Highlighter highlighter;
    private String fontStartTag = FONT_START_TAG;
    private String fontEndTag = FONT_END_TAG;

    /**
     * Instantiates a new Lucene utils.
     *
     * @throws IOException the io exception
     */
    public LuceneUtils() throws IOException {
        this(DEF_DIR);
    }

    /**
     * Instantiates a new Lucene utils.
     *
     * @param indexFile the index file
     * @throws IOException the io exception
     */
    public LuceneUtils(String indexFile) throws IOException {
        this.analyzer = new IKAnalyzer(true);
        this.indexWriter = createIndexWriter(indexFile, this.analyzer);
    }

    /**
     * Instantiates a new Lucene utils.
     *
     * @param analyzer the analyzer
     * @throws IOException the io exception
     */
    public LuceneUtils(Analyzer analyzer) throws IOException {
        this.analyzer = analyzer;
        this.indexWriter = createIndexWriter(DEF_DIR, this.analyzer);
    }

    /**
     * Instantiates a new Lucene utils.
     *
     * @param indexWriter the index writer
     * @throws IOException the io exception
     */
    public LuceneUtils(IndexWriter indexWriter) throws IOException {
        this.analyzer = new IKAnalyzer(true);
        this.indexWriter = createIndexWriter(DEF_DIR, this.analyzer);
    }

    /**
     * Instantiates a new Lucene utils.
     *
     * @param indexWriter the index writer
     * @param analyzer    the analyzer
     */
    public LuceneUtils(IndexWriter indexWriter, Analyzer analyzer) {
        this.indexWriter = indexWriter;
        this.analyzer = analyzer;
    }

    /**
     * Create index writer index writer.
     *
     * @param indexFile the index file
     * @param analyzer  the analyzer
     * @return the index writer
     * @throws IOException the io exception
     * @since 1.7
     */
    public synchronized IndexWriter createIndexWriter(String indexFile, Analyzer analyzer) throws IOException {
        Directory directory = null;
        try {
            //directory = FSDirectory.open(Paths.get(indexFile == null ? DEF_DIR : indexFile));
            directory = SimpleFSDirectory.open(Paths.get(indexFile == null ? DEF_DIR : indexFile));
        } catch (IOException e) {
            throw e;
        }
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        try {
            if (IndexWriter.isLocked(directory)) {
                //IndexWriter.unlock(directory);
                directory.obtainLock(IndexWriter.WRITE_LOCK_NAME).close();
            }
            return new IndexWriter(directory, indexWriterConfig);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Unlock.
     *
     * @throws IOException the io exception
     */
    public synchronized void unlock() throws IOException {
        if (IndexWriter.isLocked(this.indexWriter.getDirectory())) {
            //IndexWriter.unlock(this.indexWriter.getDirectory());
            this.indexWriter.getDirectory().obtainLock(IndexWriter.WRITE_LOCK_NAME).close();
        }
    }

    /**
     * Search list.
     *
     * @param text   the text
     * @param fields the fields
     * @param count  the count
     * @return the list
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    public synchronized List<Document> search(String text, String[] fields, int count) throws IOException, ParseException {
        this.highlighter = null;
        IndexSearcher searcher = getSearcher();
        QueryParser parser = new MultiFieldQueryParser(fields, this.analyzer);
        parser.setDefaultOperator(QueryParser.AND_OPERATOR);
        Query query = parser.parse(text);

        this.highlighter = null;

        TopDocs topDocs = searcher.search(query, count);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter(fontStartTag, fontEndTag);

        this.highlighter = new Highlighter(simpleHtmlFormatter, new QueryScorer(query));

        List<Document> list = new ArrayList<Document>();

        for (int i = 0; i < scoreDocs.length; i++) {
            int docId = scoreDocs[i].doc;
            Document doc = searcher.doc(docId);
            list.add(doc);
        }
        return list;
    }

    /**
     * Search list.
     *
     * @param query the query
     * @param count the count
     * @return the list
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    public synchronized List<Document> search(Query query, int count) throws IOException, ParseException {
        return search(query, count);
    }

    /**
     * Parse list.
     *
     * @param list   the list
     * @param fields the fields
     * @return the list
     * @throws IOException                  the io exception
     * @throws InvalidTokenOffsetsException the invalid token offsets exception
     */
    public List<Map<String, String>> parse(List<Document> list, String[] fields) throws IOException, InvalidTokenOffsetsException {
        List<Map<String, String>> lst = new ArrayList<Map<String, String>>();
        for (Document doc : list) {
            Map<String, String> map = new HashMap<String, String>();
            for (String key : fields) {
                String content = doc.get(key);
                TokenStream tokenStream = this.analyzer.tokenStream(key, new StringReader(content));
                content = this.highlighter.getBestFragment(tokenStream, content);
                map.put(key, content);
            }
            lst.add(map);
        }
        return lst;
    }

    /**
     * Query list.
     *
     * @param text   the text
     * @param fields the fields
     * @param count  the count
     * @return the list
     * @throws IOException                  the io exception
     * @throws ParseException               the parse exception
     * @throws InvalidTokenOffsetsException the invalid token offsets exception
     */
    public synchronized List<Map<String, String>> query(String text, String[] fields, int count) throws IOException, ParseException, InvalidTokenOffsetsException {
        List<Document> list = search(text, fields, count);
        return parse(list, fields);
    }

    /**
     * Index files string [ ].
     *
     * @return the string [ ]
     * @throws IOException the service exception
     */
    public String[] indexFiles() throws IOException {
        Directory d = indexWriter.getDirectory();
        String[] fs = new String[0];
        try {
            return d.listAll();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * List indexed list.
     *
     * @param count the count
     * @return the list
     * @throws IOException the io exception
     */
    public List<Document> listIndexed(int count) throws IOException {
        IndexSearcher indexSearcher = getSearcher();
        int size = indexWriter.maxDoc();
        if (count > 0) {
            size = size > count ? count : size;
        }
        List<Document> list = new ArrayList<Document>();
        for (int i = 0; i < size; i++) {
            Document doc = indexSearcher.doc(i);
            list.add(doc);
        }
        return list;
    }

    /**
     * Split word list.
     *
     * @param text     the text
     * @param useSmart the use smart
     * @return the list
     * @throws IOException the io exception
     */
    public List<String> splitWord(String text, boolean useSmart) throws IOException {

        StringReader re = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(re, useSmart);
        Lexeme lex = null;
        List<String> list = new ArrayList<String>();
        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }
        return list;
    }

    /**
     * Index.
     *
     * @param list the list
     * @throws Exception the exception
     */
    public synchronized void index(List<Map<String, String>> list) throws Exception {

        for (Map<String, String> map : list) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            Document doc = new Document();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                Field field = new Field(entry.getKey(), entry.getValue(), "id".equals(entry.getKey()) ? TextField.TYPE_NOT_STORED : TextField.TYPE_STORED);
                doc.add(field);
            }
            indexWriter.addDocument(doc);
        }
        indexWriter.commit();
    }

    /**
     * Gets searcher.
     *
     * @return the searcher
     * @throws IOException the io exception
     */
    public synchronized IndexSearcher getSearcher() throws IOException {
        //IndexReader indexReader = IndexReader.open(indexWriter.getDirectory());
        //return new IndexSearcher(indexReader);
        return new IndexSearcher(DirectoryReader.open(indexWriter.getDirectory()));
    }

    /**
     * Close.
     *
     * @throws IOException the io exception
     */
    public synchronized void close() throws IOException {
        if (this.analyzer != null) {
            this.analyzer.close();
        }
        if (this.indexWriter.getDirectory() != null) {
            this.indexWriter.getDirectory().close();
        }
        if (this.indexWriter != null) {
            this.indexWriter.close();
        }
    }

    /**
     * Gets analyzer.
     *
     * @return the analyzer
     */
    public Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * Sets analyzer.
     *
     * @param analyzer the analyzer
     */
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Gets index writer.
     *
     * @return the index writer
     */
    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    /**
     * Sets index writer.
     *
     * @param indexWriter the index writer
     */
    public void setIndexWriter(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }

    /**
     * Gets font start tag.
     *
     * @return the font start tag
     */
    public String getFontStartTag() {
        return fontStartTag;
    }

    /**
     * Sets font start tag.
     *
     * @param fontStartTag the font start tag
     */
    public void setFontStartTag(String fontStartTag) {
        this.fontStartTag = fontStartTag;
    }

    /**
     * Gets font end tag.
     *
     * @return the font end tag
     */
    public String getFontEndTag() {
        return fontEndTag;
    }

    /**
     * Sets font end tag.
     *
     * @param fontEndTag the font end tag
     */
    public void setFontEndTag(String fontEndTag) {
        this.fontEndTag = fontEndTag;
    }

    /**
     * Gets highlighter.
     *
     * @return the highlighter
     */
    public synchronized Highlighter getHighlighter() {
        return highlighter;
    }

    /**
     * Sets highlighter.
     *
     * @param highlighter the highlighter
     */
    public void setHighlighter(Highlighter highlighter) {
        this.highlighter = highlighter;
    }
}
