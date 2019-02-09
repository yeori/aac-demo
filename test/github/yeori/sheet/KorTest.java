package github.yeori.sheet;

import static org.junit.Assert.*;

import java.util.Collection;

import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;
import org.bitbucket.eunjeon.seunjeon.Morpheme;
import org.junit.Test;

import github.yeori.Kor;
import scala.collection.JavaConversions;

public class KorTest {

	@Test
	public void test() {
		/*
		 * ref : https://bitbucket.org/eunjeon/seunjeon/overview
		 *     : http://www.engear.net/wp/tag/mecab/
		 *     : http://www.engear.net/wp/%ED%95%9C%EA%B8%80-%ED%98%95%ED%83%9C%EC%86%8C-%EB%B6%84%EC%84%9D%EA%B8%B0-%EB%B9%84%EA%B5%90/
		 *     : https://docs.google.com/spreadsheets/d/1-9blXKjtjeKZqsf4NzHeYJCrr49-nXeRF6D80udfcwY/edit#gid=589544265
		 */
		System.out.println(new Kor().decompose("한글이다"));
		Analyzer.setUserDictFile("user-dic.csv");
		
		String text = "커피를 마십니다";
		System.out.println("[" + text + "]");
		scala.collection.Iterable<LNode> itr = Analyzer.parse(text);
		itr.foreach(elem ->{
			System.out.println(elem.morpheme());
			return elem;
		});
		
		text = "나는 학교에 갑니다.";
		System.out.println("[" + text + "]");
		for( LNode node : Analyzer.parseJava(text) ) {
			// System.out.println(node);
			Morpheme mpm = node.morpheme();
			
			System.out.printf("Morphem Surpace:  %s\n",mpm.getSurface());
			System.out.printf("Morphem feature:  %s\n", mpm.getFeatureHead());
			Iterable<Morpheme> cols = JavaConversions.asJavaIterable(mpm.deComposite());
			for(Morpheme m : cols) {
//				m.getSurface()
				System.out.printf("               :  %s(%s)\n", m.getSurface(), m.getFeature());
			}
//			mpm.deComposite().foreach((m) -> System.out.printf("               :  %s\n"));
			
//			System.out.printf("ACC cost: %d\n",node.accumulatedCost());
//			System.out.printf("ACC cost: %s\n",node.deCompoundJava().toString());
			
		}
//		Iterable<Paragraph> xxx = Analyzer.parseJavaParagraph("어제 잘 들어가셨어요? 네 늦지 않게 잘 들어갔습니다. 다행이네요.\n감사합니다.");
//		for ( Paragraph para : xxx ) {
//			System.out.println(para);
//		}
	}

}
