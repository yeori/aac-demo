package github.yeori;

public class Kor {

	static int [] cho = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
	static int [] jung = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'};
	static int [] jong = {' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};


	public String decompose(String in) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < in.length(); i++) {
			decomp(sb, in.charAt(i));
		}
		return sb.toString();
	}


	public void decomp(StringBuilder buf, char ch) {
		/*
		 * c := 글자 - 0xAC00;
		 * 초성 =  ( c-c%28) / 28 / 21
		 * 중성 = (c - c % 28 )/28%21
		 * 종성 = (글자 - 0xAC00) % 28
		 */
		int c = ch - 0xAC00;
		int c0 = (c - c%28)/28/21;
		int c1 = (c - c%28)/28%21; 
		int c2 = c%28;
		buf.append((char)cho[c0]);
		buf.append((char)jung[c1]);
		buf.append((char)jong[c2]);
	}
}
