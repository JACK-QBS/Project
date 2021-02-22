package org.example.model;

/**
 * 倒排索引 Map<String,List<Weight>>中，关键词对应的信息
 */
public class Weight {
    private DocInfo doc;
    private int weight;//权重值：通过标题和正文中，关键词的数量计算
    private String keyword;//关键词

    @Override
    public String toString() {
        return "Weight{" +
                "doc=" + doc +
                ", weight=" + weight +
                ", keyword='" + keyword + '\'' +
                '}';
    }

    public DocInfo getDoc() {
        return doc;
    }

    public void setDoc(DocInfo doc) {
        this.doc = doc;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
