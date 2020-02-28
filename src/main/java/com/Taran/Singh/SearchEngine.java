package com.Taran.Singh;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

import com.Taran.Singh.Analyzer;
import com.Taran.Singh.CranDoc;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;




public class SearchEngine {
	private IndexWriter indexWriter;
    private FSDirectory indexDir;

    public SearchEngine(String indexPath) throws IOException {
        indexDir = FSDirectory.open(Paths.get(indexPath));
    }

    private CharArraySet getStopWordSet() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResourceAsStream(File.separator + "stopWords"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        CharArraySet stopWordSet = new CharArraySet(1000, true);
        while (bufferedReader.ready()) {
            stopWordSet.add(bufferedReader.readLine());
        }
        bufferedReader.close();
        return stopWordSet;
    }

    public void open() throws IOException {
    	Analyzer analyzer = new Analyzer(getStopWordSet());
    	//StandardAnalyzer analyzer = new StandardAnalyzer(getStopWordSet());
    	//EnglishAnalyzer analyzer = new EnglishAnalyzer(getStopWordSet());
        Similarity similarity[] = {
                new BM25Similarity()
                //new ClassicSimilarity()
        };
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setSimilarity(new MultiSimilarity(similarity));
        indexWriter = new IndexWriter(indexDir, config);
    }

    public void addDocument(CranDoc cranDoc) throws IOException {
        Document document = new Document();
        document.add(new StringField("ID", cranDoc.getID(), Field.Store.YES));
        TextField titleField = new TextField("Title", cranDoc.getTitle(), Field.Store.NO);
        document.add(titleField);
        document.add(new TextField("Authors", cranDoc.getAuthors(), Field.Store.NO));
        document.add(new TextField("Department", cranDoc.getDepartment(), Field.Store.NO));
        TextField abstractField = new TextField("Abstract", cranDoc.getAbstr(), Field.Store.NO);
        document.add(abstractField);
        indexWriter.addDocument(document);
    }


    public ArrayList<SearchResult> search(String cranQuery) throws IOException, ParseException {
        DirectoryReader indexReader = DirectoryReader.open(indexDir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Similarity similarity[] = {
                new BM25Similarity()
                //new ClassicSimilarity()
        };
        indexSearcher.setSimilarity(new MultiSimilarity(similarity));
        String fields[] = {"Title", "Abstract"};
        Analyzer analyzer = new Analyzer(getStopWordSet());
        //StandardAnalyzer analyzer = new StandardAnalyzer(getStopWordSet());
        //EnglishAnalyzer analyzer = new EnglishAnalyzer(getStopWordSet());
        QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        String queryString = QueryParser.escape(cranQuery);
        Query query = queryParser.parse(queryString);
        TopDocs topDocs = indexSearcher.search(query, 1000);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<SearchResult> resultDocs = new ArrayList<>();
        int i = 1;
        for (ScoreDoc scoreDoc : scoreDocs) {
            String currentID = indexSearcher.doc(scoreDoc.doc).get("ID");
            SearchResult currentDoc = new SearchResult(currentID, scoreDoc.score, i++);
            resultDocs.add(currentDoc);
        }
        return resultDocs;
    }

    public void close() throws IOException {
        indexWriter.close();
    }

}
