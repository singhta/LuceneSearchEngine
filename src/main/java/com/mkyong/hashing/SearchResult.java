package com.mkyong.hashing;


public class SearchResult {
	private String id;
    private float score;
    private int rank;


    public SearchResult(String id, float score, int rank) {
        this.id = id;
        this.score = score;
        this.rank = rank;
    }


    public String getID() {
        return id;
    }

    public float getScore() {
        return score;
    }


    public int getRank() {
        return rank;
    }
}
