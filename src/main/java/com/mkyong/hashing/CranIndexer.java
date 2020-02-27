package com.mkyong.hashing;


import java.io.*;


import com.mkyong.hashing.CranDoc;
import com.mkyong.hashing.CranQuery;


public class CranIndexer {
    private String docsPath;
    private String queriesPath;
    public CranIndexer(String docsPath, String queriesPath) {
        this.docsPath = docsPath;
        this.queriesPath = queriesPath;
    }
    public void createDocs() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResourceAsStream(File.separator + "cran.all.1400"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String doc = bufferedReader.readLine();
        int i = 1;
        while (doc != null) {
            FileWriter fileWriter = new FileWriter(docsPath + File.separator + "cran.doc." + i);
            fileWriter.append(doc).append("\n");
            doc = bufferedReader.readLine();
            while (doc != null && !doc.startsWith(".I")) {
                fileWriter.append(doc).append("\n");
                doc = bufferedReader.readLine();
            }
            i++;
            fileWriter.close();
        }
        bufferedReader.close();
    }
    public void createQueries() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResourceAsStream(File.separator + "cran.qry"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String query = bufferedReader.readLine();
        int i = 1;
        while (query != null) {
            FileWriter fileWriter = new FileWriter(queriesPath + File.separator + "cran.qry." + i);
            fileWriter.append(query).append("\n");
            query = bufferedReader.readLine();
            while (query != null && !query.startsWith(".I")) {
                fileWriter.append(query).append("\n");
                query = bufferedReader.readLine();
            }
            i++;
            fileWriter.close();
        }
        bufferedReader.close();
    }
    public CranDoc makeDoc(int id) throws IOException {
        FileReader fileReader = new FileReader(new File(docsPath + File.separator + "cran.doc." + id));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String doc = bufferedReader.readLine();
        doc = bufferedReader.readLine();
        StringBuilder title = new StringBuilder();
        StringBuilder authors = new StringBuilder();
        StringBuilder department = new StringBuilder();
        StringBuilder abstr = new StringBuilder();
        while (doc != null) {
            switch (doc) {
                case ".T":
                    doc = bufferedReader.readLine();
                    while (!doc.equals(".A")) {
                        title.append(doc).append("\n");
                        doc = bufferedReader.readLine();
                    }
                    break;
                case ".A":
                    doc = bufferedReader.readLine();
                    while (!doc.equals(".B")) {
                        authors.append(doc).append("\n");
                        doc = bufferedReader.readLine();
                    }
                    break;
                case ".B":
                    doc = bufferedReader.readLine();
                    while (!doc.equals(".W")) {
                        department.append(doc).append("\n");
                        doc = bufferedReader.readLine();
                    }
                    break;
                case ".W":
                    doc = bufferedReader.readLine();
                    while (doc != null) {
                        abstr.append(doc).append("\n");
                        doc = bufferedReader.readLine();
                    }
                    break;
                default:
                    break;
            }
        }
        bufferedReader.close();
        String currentId = Integer.toString(id);
        return new CranDoc(currentId, title.toString(), authors.toString(), department.toString(), abstr.toString());
    }
    public CranQuery makeQuery(int id) throws IOException {
        FileReader fileReader = new FileReader(new File(queriesPath + File.separator + "cran.qry." + id));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String query = bufferedReader.readLine();
        query = bufferedReader.readLine();
        query = bufferedReader.readLine();
        StringBuilder abstr = new StringBuilder();
        while (query != null) {
            abstr.append(query).append("\n");
            query = bufferedReader.readLine();
        }
        bufferedReader.close();
        String currentId = Integer.toString(id);
        return new CranQuery(currentId, abstr.toString());
    }
}
