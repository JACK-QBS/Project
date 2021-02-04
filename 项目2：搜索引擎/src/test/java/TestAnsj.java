import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

public class TestAnsj {
    public static void main(String[] args) {
        String str = "小明毕业于清华大学计算机专业," +
                "后来去蓝翔技校和新东方深造," +
                "擅长使用计算机控制挖掘机炒菜";
        List<Term> terms = ToAnalysis.parse(str).getTerms();
        for (Term term : terms) {
            System.out.print(term.getName() + "/");
        }
    }
}
