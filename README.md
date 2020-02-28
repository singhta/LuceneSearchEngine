1) Change directory to "LuceneSearchEngine" -- cd "LuceneSearchEngine"
2) Run "mvn clean install" to build the code.
3) Run "mvn exec:java -Dexec.mainClass="com.Taran.Singh.CranSearcher" to run the mainClass
4) Results will be stored in carnfield/results
5) To run trec_eval , change directory to cranfield and run "./trec_eval QRelsCorrectedforTRECeval results"
