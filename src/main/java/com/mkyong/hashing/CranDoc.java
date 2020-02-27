package com.mkyong.hashing;


public class CranDoc {
    private String id;
    private String title;
    private String authors;
    private String department;
    private String abstr;
      
    public CranDoc(String id, String title, String authors, String department, String abstr) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.department = department;
        this.abstr = abstr;
    }
    public String getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getAuthors() {
        return authors;
    }


    public String getDepartment() {
        return department;
    }


    public String getAbstr() {
        return abstr;
    }
}