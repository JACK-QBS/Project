package org.example.util;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.example.model.DocInfo;
import org.example.model.Weight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 构建索引:
 * 正排索引：从本地文件数据中读取到 java 内存（类似于数据库保存的数据）
 * 倒排索引：构建 Map<String,List<信息>>（类似数据库 hash 索引）
 * Map键：关键词（分词来做）
 * Map值 - 信息：
 * （1）docInfo 对象引用或是 docInfo 的 id
 * （2）权重（标题对应的关键词数量*10 + 正文对应关键词数量*1）（自定义）
 * （3）关键词
 */
public class Index {

    //正排索引：
    private static final List<DocInfo> FORWARD_INDEX = new ArrayList<>();
    //倒排索引：
    private static final Map<String,List<Weight>> INVERTED_INDEX = new HashMap<>();

    /**
     * 构建正排索引的内容：从本地 raw_data.txt 中读取并保存
     */
    public static void buildForwardIndex() {
        try {
            FileReader fr = new FileReader(Parser.RAW_DATA);
            BufferedReader br = new BufferedReader(fr);
            int id = 0;//行号设置为 docInfo 的 id
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) continue;
                //一行对应一个 DocInfo 对象，类似数据库一行数据对应Java对象
                DocInfo doc =  new DocInfo();
                doc.setId(++id);
                String[] parts = line.split("\3");//每一行按 \3 间隔符切开
                doc.setTitle(parts[0]);
                doc.setUrl(parts[1]);
                doc.setContent(parts[2]);
                //添加到正排索引
                System.out.println(doc);
                FORWARD_INDEX.add(doc);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建倒排索引：从java内存中正排索引获取文档来构建
     */
    public static void buildInvertedIndex() {
        for (DocInfo doc : FORWARD_INDEX) {//doc+分词 对应 weight（doc和分词一对多，分词和weight一对一）
            //一个doc，分别对标题和正文分词，每一个分词生成一个weight对象，需要计算权重
            //如标题为：清华大学/计算机/专业/使用/计算机/炒菜
            //第一次出现的关键词，要new Weight对象，之后出现相同分词关键词时
            // 要获取之前已经拿到的相同关键词weight对象，再更新权重（把自己的权限加进去）
            //实现逻辑：先构造一个HashMap，保存分词（键）和weight对象（value）
            Map<String,Weight> cache = new HashMap<>();

            //标题 分词遍历处理
            List<Term> titleFenCis = ToAnalysis.parse(doc.getTitle()).getTerms();
            for (Term titleFenCi : titleFenCis) {
                Weight w = cache.get(titleFenCi.getName());//获取标题分词键对应的weight
                //如果没有，就创建一个并放到map中
                if (w == null) {
                    w = new Weight();
                    w.setDoc(doc);
                    w.setKeyword(titleFenCi.getName());
                    cache.put(titleFenCi.getName(),w);
                }
                //标题分词，权重就+10
                w.setWeight(w.getWeight()+10);
            }

            //正文 分词遍历处理
            List<Term> contentFenCis = ToAnalysis.parse(doc.getContent()).getTerms();
            for (Term contentFenCi : contentFenCis) {
                Weight w = cache.get(contentFenCi.getName());//获取标题分词键对应的weight
                //如果没有，就创建一个并放到map中
                if (w == null) {
                    w = new Weight();
                    w.setDoc(doc);
                    w.setKeyword(contentFenCi.getName());
                    cache.put(contentFenCi.getName(),w);
                }
                //正文分词，权重就+1
                w.setWeight(w.getWeight()+1);
            }

            //把临时保存的map数据（keyword-weight）全部保存到倒排索引
            for (Map.Entry<String,Weight> e : cache.entrySet()) {
                String keyword = e.getKey();
                Weight w = e.getValue();
                //更新保存到倒排索引 Map<String,List<Weight>> --> 多个文档，同一个关键词，保存在一个List
                //先在倒排索引中，通过keyword获得已有的值
                List<Weight> weights = INVERTED_INDEX.get(keyword);
                //如果拿不到，就创建一个，并存放到倒排索引
                if (weights == null) {
                    weights = new ArrayList<>();
                    INVERTED_INDEX.put(keyword,weights);
                }
                //System.out.println(keyword+": （"+w.getDoc().getId()+", "+w.getWeight()+"） ");
                weights.add(w);//倒排中，添加当前文档每个分词对应的weight对象
            }
        }
    }

    //通过关键词（分词）在倒排中查找映射的文档（多个文档，倒排拉链）
    public static List<Weight> get(String keyword) {
        return INVERTED_INDEX.get(keyword);
    }

    public static void main(String[] args) {

        Index.buildForwardIndex();

        //测试正排索引构建
        //FORWARD_INDEX.stream().forEach(System.out::println);

        //测试倒排内容是否正确
        for (Map.Entry<String,List<Weight>> e : INVERTED_INDEX.entrySet()) {
            String keyword = e.getKey();
            System.out.print(keyword+": ");
            List<Weight> weights = e.getValue();
            weights.stream()
                    .map(w->{//map操作：把list中每一个对应转换为其他对象
                        return "（"+w.getDoc().getId()+", "+w.getWeight()+"）";
                    })//转换完，会变成List<String>
//                    .collect(Collectors.toList());//返回List<String>
                    .forEach(System.out::print);
            System.out.println();
        }
    }

}
