

import java.io.*;

/**
 ** SkScanner: 김성기 교수가 작성한 입력을 위한 클래스
 **    o 이 SkScanner 클래스는 자유롭게 입력된 데이터 중에서 문자, 정수, 실수, 문자열, boolean, 식별자를 
 **        찾아서 입력하는 메소드들을 정의
 **    o SkScanner 클래스의 객체를 생성하지 않고 SkScanner 클래스의 메소드를 사용할 수 있도록 
 **        모든 메소드는 static 메소드
 **
 **    o 사용 예 :  
 **        char c = SkScanner.getChar(); (또는 SkScanner.getchar(); - 이는 C 언어에서 제공되기 때문)
 **        int n = SkScanner.getInt();             // 입력되는 문자열에서 첫 번째 정수를 찾아 int 값으로 반환
 **        int n = SkScanner.getLong();            // 입력되는 문자열에서 첫 번째 정수를 찾아 long 값으로 반환
 **        double d = SkScanner.getDouble()        // 입력되는 문자열에서 첫 번째 실수를 찾아 double 값으로 반환
 **        float f = SkScanner.getFloat()          // 입력되는 문자열에서 첫 번째 실수를 찾아 float 값으로 반환
 **        String name = SkScanner.getString();    // '\n'이 입력될 때까지 입력된 문자들을 String 값으로 반환
 **        boolean bool = SkScanner.getBoolean();  // 입력되는 문자열에서 true 또는 false를 찾아 boolean 값으로 반환
 **        String id = SkScanner.getIdentifier();  // 입력되는 문자열에서 Java의 식별자를 찾아 String 값으로 반환
 **                                                //   식별자의 첫 글자: (영문자 한글 _ $), 꼬리부분: (영문자 한글 숫자 _ $)
 **
 **        char c = SkScanner.getCharNonWhite();   // 공백문자(white space: blank, tab, newline 등)를 제외한 첫 문자를 입력 
 *
 **    o 최종 수정: 2019년 10월 19일                                            
 ** 
 **    o 참고사항
 **      (1) 이 클래스의 메소드들은 표준입력이 키보드인 경우에는 잘 동작한다. 
 **          그런데 표준입력이 키보드가 아니고 파일 등으로 변경된 상태에서 입력 메소드가 호출되면
 **          처리 중에 getcharPrivate()에서 EOF(End Of File) 문자를 만나면 -1이 반환되며, 
 **          이때의 입력은 제대로 처리되지 않고 오류가 발생할 수 있음
 **          이를 위해 예외 처리를 할 수 있으나 간편하게 사용하게 하기 위해 예외 처리는 하지 않았음
 *
 *       (2) getString() 메소드는 엔터키가 입력되기 전까지 입력된 문자들을 문자열로 반환하는 메소드이므로
 *           java.util.Scanner 클래스의 nextLine() 메소드와 동일한 기능의 메소드임
 *           
 *           java.util.Scanner 클래스의 next() 메소드에 해당되는 메소드는 없음
 *                                                        
 */
 
public class SkScanner { 
	final static int MAX_LENGTH_OF_PEEKCHARS = 100;
	final static int MAX_LENGTH_OF_STRING = 2000;
	
	static java.io.BufferedReader in;

	static {
		try {
			// Windows에서 한글을 입력하기 위해 문자셋을 "ms949"로 지정
			in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in, "ms949")); 
		} catch(Exception e) { 
			in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		}
	}
	
	private static int[] peekChars = new int[MAX_LENGTH_OF_PEEKCHARS]; // 최대 100개의 읽은 문자를 다시 읽기 위해 저장하는 peekChars 배열 
	private static int peekcIndex = -1;	       // peekChars 배열에서의 마지막 저장 위치, -1이면 저장된 문자가 없음 
	
	private static int sign;  // 읽은 정수 또는 실수의 부호를 기록하기 위한 변수

	private SkScanner()  {
	}

	// getcharPrivate() : 한 문자를 읽기 위한 내부 전용 메소드. 
	//   peekcIndex가 -1이 아니면 peekChars[peekcIndex]를 반환
	//   peekcIndex가 -1이면 in.read()를 이용하여 한 문자를 읽어들임
	private static int getcharPrivate() {	
		int retval;

		try {
			if (peekcIndex >=  0) { 
				// peekcIndex가 -1이 아니면 peekChars[peekcIndex]를 반환하고, peekcIndex 감소
				retval = peekChars[peekcIndex--]; 

				return retval;
			}

			return in.read();  // 키보드에서 입력된 문자를 읽어 반환
		} catch (IOException e)  {  return -1;}
	}


	// ungetc(): in.read()를 이용하여 이미 입력한 문자를 다시 읽을 수 있도록 peekChars 배열에 저장하는 메소드
	private  static void ungetc(int c) {	
		peekChars[++peekcIndex] = c;	// peekcIndex를 1 증가한 후 주어진 문자를 peekChars[peekcIndex]에 저장
	} 

	// skipUntilDigit() : 첫 디지트가 나올 때까지 문자들을 skip하여 첫 디지트를 반환하는 메소드
	//   단 음수부호가 디지트 앞에 나올 때에는 sign 필드를 -1로 만든다.
	private static int skipUntilDigit() {	
		int c;

		sign = 1;    // sign은 +로 시작하게 함
		while((c=getcharPrivate()) != -1)  {
			if (c>='0' && c <= '9')  // 읽은 문자가 디지트이면 이를 반환
				return c;
			else if (c == '-')       // 읽은 문자가 '-'이면 sign을 음수(-1)로
				sign = -1;	     	
			else
				sign = 1;	         // 그외는 무시하고 sign을 양수(1)로 set
		}

		return -1;
	}
	
	// skipUntilDigitOrDot() : 첫 디지트 또는 '.'가 나올 때까지 문자들을 skip하여 첫 디지트를 반환하는 메소드
	//   단 음수부호가 디지트 앞에 나올 때에는 sign 필드를 -1로 만든다.
	//   스킵을 할 때 부호(+/-)가 나타나면 이를 표시하기 위해 sing 변수를 1/-1으로 세팅함
	//   실수에서 +.123 또는 -.123 등의 패턴을 위해 '.' 이전에 입력된 부호도 처리함 
	private static int skipUntilDigitOrDot() {	
		int c, cPriv = -1;

		sign = 1;    // sign은 +로 시작하게 함
		
		while((c=getcharPrivate()) != -1)  {
			if (c>='0' && c <= '9' || c == '.')  // 읽은 문자가 디지트 또는 '.'이면 이를 반환
				return c;
			else if (c == '+' || (c=='.' && cPriv=='+'))         // 읽은 문자가 '+' 또는 "+."이면 sign을 양수(1)로
				sign = 1;	     	
			else if (c == '-' || (c=='.' && cPriv=='-'))         // 읽은 문자가 '-' 또는 "-."이면 sign을 음수(-1)로
				sign = -1;	     	
			else 
				sign = 1;	     // 그외는 무시하고 sign을 양수(1)로 set
			
			cPriv = c;
		}

		return -1;
	}

	// removeLastNewLineChar(): 정수, 실수, 문자열, 식별자 등을 읽기 위해 입력된 '\r', '\n'을 제거시킴
	//   정수, 실수, 문자열, 식별자 등을 읽기 위해 입력된 마지막 '\r', '\n'을 제거시켜 
	//   다음에 getChar()를 했을 때 '\r', '\n'이 읽혀지지 않게 함
	//   이 메소드는 반드시 getLong(), getDouble(), getString(),  getIdentifier()메소드의 마지막에서 호출되어야 함
	static void removeLastNewLineChar() {
		int c1, c2;
		if ( (c1 = getcharPrivate()) == '\r') {
			if ( (c2 = getcharPrivate()) == '\n')		
				return;  // '\r', '\n'을 읽어 제거시킴 
			else {
				ungetc(c2);
				ungetc(c1);
			}
		} else
			ungetc(c1);
	}

	// getChar() : 표준입력에서 문자를 읽어 반환하는 메소드
	//   
	public static  char getChar() {
		return (char) getcharPrivate();
	}

	// getchar() : 표준입력에서 문자를 읽어 반환하는 메소드
	//   
	public static char getchar() {
		return getChar();
	}
	
	// getCharNonWhite() : 표준입력에서 공백 문자를 제외하고서 첫 번째 문자를 읽어 반환하는 메소드
	//   
	public static char getCharNonWhite() {
		
		while(true) {
			int c = getcharPrivate();
			
			if (c != ' ' && c != '\t' && c != '\n')
				return (char) c;
		}
	}

	// getLong() : 표준입력에서의 정수 문자들을 읽어 long 타입의 값으로 변환하여 반환하는 메소드
	//     양수뿐 아니라 음수도 처리, 정수 앞의 디지트가 아닌 문자는 스킵함
	//     정수 중간에는 _도 가능함(1_234_567, 1_____234______567)
	public static long getLong() {
		int c;

		c = skipUntilDigit();   // 첫 번째 디지트를 얻을 때까지 문자를 읽어 무시함		
		ungetc(c);              // 읽은 첫 번째 디지트를 다시 읽기 위해 ungetc시킴
		
		long l = getLongWithoutSkip(); // 스킵 없이 정수를 읽어 long 값으로 반환
		
		removeLastNewLineChar();       // 정수, 실수, 문자열, 식별자 등을 읽기 위해 입력된 '\r', '\n'을 제거시킴
		return l;
	}
		
	// getInt() : 표준입력에서의 정수 문자들을 읽어 int 정수로 변환하여 반환하는 메소드
	//     양수뿐 아니라 음수도 처리, 정수 앞의 디지트가 아닌 문자는 스킵함
	public static  int getInt() {
		return (int) getLong() ;
	}

	// getLongStringWithoutSkip() : 표준입력에서의 스킵없이 디지트 문자들을 읽어 String 타입의 값으로 변환하여 반환하는 메소드
	//     디지트가 아닌 문자는 모두 스킵된 상태에서 호출됨
	//     Java에서는 정수 중간에 _를 허용하여 긴 자리수의 정수를 읽기 쉽게 표현(1_234_567, 1_____234______567)하므로
	//     정수 중간의 _는 무시함
	private static String getLongStringWithoutSkip() {
		int c;
		String s = "";

		int countUnderScore = 0;  // 정수 중간에 나타나는 연속된 _의 개수

		while((c = getcharPrivate()) >='0' && c <='9' || c == '_')  { // 연속되는 디지트 또는 _에 대하여			
			if (c >= '0' && c <= '9') {
				s = s + (char) c;         //   문자열에 접속하여 변환
				countUnderScore = 0;
			}
			else if (c == '_') {  // -가 입력되면
				countUnderScore++; // 연속된 _의 개수를 1 증가시킴
			}
		}

		if (countUnderScore > 0) // 정수가 끝난 후 입력된 _를 모두 ungetc시킴
			for (int i=0; i<countUnderScore; i++)
				ungetc('_');

		
		ungetc(c);           // 마지막 읽은 문자는 다음 getcharPrivate()이 읽도록 보관
				
		return s ;
	}
	
	// getLongWithoutSkip() : 표준입력에서의 스킵없이 디지트 문자들을 읽어 long 타입의 값으로 변환하여 반환하는 메소드
	//     양수뿐 아니라 음수도 처리, 디지트가 아닌 문자는 모두 스킵된 상태에서 호출됨
	//     이 메소드가 호출되기 전에 부호가 처리되어 sign 변수에 저장되어 있으므로 sign을 곱해줌 
	private static  long getLongWithoutSkip() {
		return sign * Long.parseLong(getLongStringWithoutSkip());
	}
	// getintWithoutSkip() : 표준입력에서의 스킵없이 정수문자들을 읽어 int 타입의 값으로 변환하여 반환하는 메소드
	//     양수뿐 아니라 음수도 처리, 디지트가 아닌 문자는 모두 스킵된 상태에서 호출됨
	private static  int getIntWithoutSkip() {
		return (int) getLongWithoutSkip();
	}

	// getDouble() : 23.45, -12.3, -0.0123, +.01234, -.1234e33 1.234e+12, 1.234E-12 등
	//  Java의 실수 값을 입력하여 double 값으로 반환하는 메소드
	//    실수 형식: "[+/-정수부].[소수부]E/e[+/-지수부]" 
	//
	//    실수 입력을 위해 반드시 숫자가 . 전에 나타나든지 . 다음에 숫자가 나타나야 함
	//    올바른 실수를 입력하는 것은 다소 어려움
	//       예를 들어 "...---.-12.345.012"이 입력되면 -12.345으로 입력해야하므로
	//    정수부가 없으면서 '.' 다음에 소수부 디지트가 입력되지 않으면 다시 실수 입력이 시작되어야 한다. 
	public static double getDouble() {
		long n = 0;          // 정수부 저장할 변수 
		double d = 0;       // 소수부 저장할 변수
		long exp = 0;        // 지수부 저장할 변수
		
		int signInt = 1;    // 부호를 저장할 변수
		int c;

		c = skipUntilDigitOrDot();   // 첫 번째 디지트 또는 '.'를 얻는다	
		signInt = sign;       // 정수부의 부호를 저장

		ungetc(c);           // 방금 읽은 문자를 다시 읽기 위해 ungetc()

//		System.out.println("  <for debug >c1 = " + c + " " + (char) c);
		
		boolean isThereIntPart = false;          // 정수부 존재 유무를 저장
		boolean isThereUnderPointPart = false;  // 소수부 존재 유무를 저장
		
		if (c>='0' && c<='9') { // 정수부가 존재하면
			n = getLongWithoutSkip();         // 디지트 아닌 문자에 대한 스킵 없이 정수부 입력
			isThereIntPart = true;
		}

		c = getcharPrivate();  // 소수부를 확인하기 위해 문자를 읽음

		if (c=='.') {   // 다음에  '.' 문자가 다음에 나타나면 소수부가 시작될 가능성이 있으므로 
			int c2 = getcharPrivate();
			if (c2>='0' && c2 <='9') {      // '.' 문자 다음 문자가 숫자 디지트 문자이면 소수부가 존재하므로 이를 처리
			    ungetc(c2);                 // 방금 읽은 문자를 다시 읽기 위해 ungetc()
				d = getDoubleUnderPoint();  // 소수부를 입력
				
				isThereUnderPointPart = true;
			}
			else {      // '.' 다음에 소수부가 나타나지 않는 경우이므로
				if (isThereIntPart) {  // 정수부가 있으면 소수부는 0임
					d = 0;  // 소수부는 0
					ungetc(c2);           // 읽은 문자를 다시 읽기 위해 저장
				}
				else {  // 정수부도 없고 소수부도 없으면 실수가 입력이 안된 상태이므로 '.'인 c는 무시하고 다시 실수 입력 시도
					ungetc(c2);           // 읽은 문자를 다시 읽기 위해 저장
					return getDouble();   // 지금까지는 입력을 무시하고 새로운 실수를 입력하므로 재귀 호출이 사용됨
				}
			}		
		}
		else
			ungetc(c); // 소수부가 없으면 읽은 문자를 ungetc()
		
		c = getcharPrivate();  // 지수부를 확인하기 위해 문자를 읽음
		if (c=='e' || c=='E') { // e 또는 E로 지수부가 시작되면
			int c2 = getcharPrivate();
			
			if (c2 >= '0' && c2 <= '9') {  // 부호가 없는 지수부가 있으면
				ungetc(c2); // 읽은 디지트 문자를 다시 읽기 위해 저장
				
				exp = getIntWithoutSkip();         // 디지트 아닌 문자에 대한 스킵 없이 정수부 입력하여 지수부 값으로 저장				
			}
			else { // 부호 있는 지수부가 있는가를 체크하여 처리함
				if (c2 == '+' || c2 == '-') {
					int c3 = getcharPrivate();
					if (c3 >= '0' && c3 <= '9') {  // +/- 다음에 지수부인 정수가 있으면
						ungetc(c3); // 읽은 문자를 다시 읽기 위해 저장

						exp = getIntWithoutSkip(); // 디지트 아닌 문자에 대한 스킵 없이 정수부 입력하여 지수부 값으로 저장			
						exp = exp * ( (c2 == '+') ? 1 : -1 ) ;   // +지수부는 1, -지수부는 -1을 곱함	
					}
					else {  // e로 시작하고 +/-가 입력되었으나  +/- 다음에 숫자 아니면 지수부가 없는 것으로 처리 
						ungetc(c3);
						ungetc(c2);
						ungetc(c);
					}
				}
				else { // e로 시작했으나 숫자, + , -가 아니면 지수부가 없는 것으로 처리
					ungetc(c2);
					ungetc(c);				
				}
			}
	    }
		else
			ungetc(c); // 지수부가 없으면 읽은 문자를 ungetc() 

		if (signInt == 1) 	// 읽은 정수부의 부호에 따라
			d = n + d;	    // 부호가 양수이면 소수부를 정수부에 단순히 더해줌
		else 
			d = n - d;     // 부호가 음수이면 소수부를 정수부에서 빼주어야 함

		if (exp != 0)      //  지수부가 있으면 지수부만큼 곱해줌
			d = d * power10(exp);
		
		removeLastNewLineChar();  // 정수, 실수, 문자열, 식별자 등을 읽기 위해 입력된 '\r', '\n'을 제거시킴
		
		return d;
	}

	// getFoloat() : 23.45, -12.3, -0.012_3, +.012_34, -.1234e3_3 1.234e+12, 1.234E-1_2 등
	//    Java의 실수 값을 입력하여 float 값으로 반환하는 메소드
	//    o 실수 형식: "[+/-정수부].[소수부]E/e[+/-지수부]" 
	public static float getFloat() {
		return (float) getDouble();
	}

	// 소수점 이하의 실수부를 읽어들임
	private static double getDoubleUnderPoint() {
		String longString = getLongStringWithoutSkip();  // 소수점 이하의 정수를 읽어 문자열로 반환
		
		long  nUnerPoint  = Long.parseLong(longString);  // 소수점 이하의 long 값을 구함
		int noDigit = longString.length();   // 소수점 이하의 정수부 자리수 구함		 

		return  (double) nUnerPoint / power10(noDigit);  // '.' 이하 정수부를 10^자리수로 나누어 소수부로 반환
	}

	// 주어진 정수의 자리수를 구함하여 반환
	public static int getNoDigit(int n) {
		int no = 0;

		while (n != 0) {
			no++;
			n = n / 10;
		}

		return no;
	}

	// 10의 n 지수값을 반환
	public static double power10(long n) {
		double pow = 1;

		if (n > 0) {
			while(n-- > 0) 
				pow = pow * 10;
		}
		else if (n < 0){
			while(n++ < 0) 
				pow = pow / 10;
		}

		//	System.out.println("\n  <for debug >n = " + n + ", exp = " + pow);
		
		return pow;
	}

    // nextInt(): Java의 Scanner 클래스에 있는 메소드와 같음
	public static int nextInt() {
		return getInt();
	}

	// getBoolean() : "true" 또는 "false"를 입력하여 true 또는 false를 boolean 값으로 반환하는 메소드
	//    "true" 또는 "false"를 입력하기 위해 식별자가 반드시 먼저 나타나야 함
	//    입력된 식별자가 "true" 또는 "false"이면 boolean 타입의 true 또는 false 를 반환,
	//    그렇지 않은 경우 다음 식별자를 입력하여 확인을 반복함
	public static boolean getBoolean()  {
		while (true) {
			String id = getIdentifier();
			
//			System.out.println("  o identifier = '" + id + "'"); 
			
			if (id.equals("true"))
				return true;
			else if (id.equals("false"))
				return false;			
		}
	}

	// getIdentifier() : Java의 식별자를 입력하여 반환하는 메소드
	//  식별자의 첫 문자: 영문자, 한글, '_', '$'
	//  식별자의 꼬리부분: 영문자, 한글, 숫자, '_', '$'
	//  (그리스 문자, 중국어 문자, 일본어 문자 등도 식별자에 포함되나 여기에서는 처리하지 않음)
	public static String getIdentifier()  {
		int head = skipUntilFirstCharOfIdentifier(); // 식별자의 첫 문자를 찾아 입력하여 반환
//		System.out.println("  o head of identifier = '" + (char) head + "' (" + head + ")"); 
		
		String identifier = getTailOfID(head); // 식별자의 꼬리부분을 입력하여 첫 문자 head와 결합하여 반환
		
		removeLastNewLineChar();  // 정수, 실수, 문자열, 식별자 등을 읽기 위해 입력된 '\r', '\n'을 제거시킴

		return identifier;		
	}
	
	// skipUntilFirstCharOfIdentifier() : 식별자의 첫 문자인 영문자, 한글, '_', '$'가 나올 때까지 
	//   문자들을 skip하여 식별자의 첫 문자를 반환하는 메소드
	public static int skipUntilFirstCharOfIdentifier() {
		int c;

		while(true)  {
			c = getcharPrivate();
			
			if (isAlphabet(c))      // 읽은 문자가 알파베트이면 이는 식별자 첫 문자에 해당, 이를 반환
				return c;
			else if (c == '_' || c=='$')    // 읽은 문자가 '_' 또는 "$"이면 이는 식별자 첫 문자에 해당, 이를 반환
				return c;    	
		}
	}

	// isAlphabet() : 주어진 문자가 Java의 식별자의 알파베트에 해당되는지를 검사하는  메소드
	//  알파베트: 영문자, 한글, '_', '$'
	//  (그리스 문자, 중국어 문자, 일본어 문자 등도 알파베트에 포함되나 여기에서는 처리하지 않음)
   public static boolean isAlphabet(int c) {
		if (c>='A' && c <= 'Z' || c>='a' && c <= 'z' )  // 읽은 문자가 영문자이면 이를 반환
			return true;
		// 유니코드에서 한글 자음과 모음은 'ㄱ'(12593)에서 'ㅣ'(12643)까지 배정됨
		// 유니코드에서 한글 문자는 '가'('\uAC00': 44032)에서 '힣'('\uD7A3': 55203)까지 배정됨
		else if (c >= 12593 && c <= 12643)   // 읽은 문자가 한글 자음 또는 모음이면 이는 식별자 첫 문자에 해당됨
			return true;
		else if (c >= 44032 && c <= 55203)   // 읽은 문자가 한글 음절이면 이는 식별자 첫 문자에 해당됨
			return true;

		return false;
    	
    }
    
	// getTailOfID(int head): Java 식별자의 꼬리부분을 입력하여 주어진 헤드문자 head와 함께 전체 식별자를 구성하여 문자열로 반환
	//  식별자의 첫 문자: 영문자, 한글, '_', '$'
	//  식별자의 꼬리부분: 영문자, 한글, 숫자, '_', '$'
	public  static String getTailOfID(int head) {
		int i=0;

		int c;

		char cs[] = new char[MAX_LENGTH_OF_STRING];
 
		cs[0] = (char) head;
		i++;
		
		while(true)  {
			c = getcharPrivate();
//			System.out.println("  o tail of identifier = '" + (char) c + "' (" + c + ")"); 
			
			if (isAlphabet(c))              // 읽은 문자가 알파베트이면 이는 식별자 꼬리에 해당됨
				cs[i++] = (char) c; 
			else if (c == '_' || c=='$')    // 읽은 문자가 '_' 또는 "$"이면 이는 식별자 꼬리에 해당됨
				cs[i++] = (char) c;   	
			else if (c >= '0' && c<='9')    // 읽은 문자가 디지트이면 이는 식별자 꼬리에 해당됨
				cs[i++] = (char) c;    	
			else
				break;
		}
		
		ungetc(c); // 마지막 읽은 문자는 식별자 문자 아니므로 다음에 읽을 수 있도록 ungetc시킴

		cs[i] = 0;

		String s =  new String(cs, 0, i);  // 문자 배열을 문자열로 변환 

		return s;	

	}  
	

	// getString() : '\n', '\r', -1이 입력될 때까지 입력된 문자들을 String 값으로 반환
	//    엔터키가 '\n', '\r' 두개의 문자로 이루어지는 경우를 위해 
	//    처음에 나타나는 '\n', '\r' 문자는 무시한다.  
	public  static String getString()  {
		int i=0;

		int c;

		char cs[] = new char[MAX_LENGTH_OF_STRING];

		while((c = getcharPrivate()) != '\n' &&  c != '\r' && c != -1) 
			cs[i++] = (char) c;
		ungetc(c);
		
		cs[i] = 0;

		String s =  new String(cs, 0, i);
		
		removeLastNewLineChar();  // 정수, 실수, 문자열, 식별자 등을 읽기 위해 입력된 '\r', '\n'을 제거시킴

		return s;	 
	}
	
	// 디지트 문자가 입력될 때까지의 문자들을 읽어 String 문자열을 반환
	public  static String getStringUntilNotDigit()  {
		int i=0;

		int c;

		char cs[] = new char[MAX_LENGTH_OF_STRING];


		while((c =  getcharPrivate()) >= '0' &&  c <= '9' ) 
			cs[i++] = (char) c;
		
		ungetc(c);  // 마지막 읽은 문자를 다시 읽을 수 있도록 ungetc()

		cs[i] = 0;

		String s =  new String(cs, 0, i);  // 문자배열을 String 객체로 변환

		return s;	 
	}

	// nextString(): Java의 Scanner 클래스에 있는 메소드와 같음
	public static String nextString() {
		return getString();
	} 


	public static String toKSC5601(String str) // throws java.io.UnsupportedEncodingException 
	{ 

		if (str == null) return null; 

		try {
			return new String(str.getBytes("8859_1"), "KSC5601"); 
		}
		catch(Exception e) {}

		return null;

	} 
	

	/*
	 * 프롬프트 메시지를 출력한 뒤 데이터 입력하는 메소드들
	 * 
	 * 입력할 때 프롬프트 메시지 없이 입력을 하면 사용자는 막연히 기다리는 경우가 발생한다.
	 * 이러한 경우를 방지하기 위해 입력 시에는 메시지를 미리 출력하여 
	 * 입력할 데이터의 용도나 기능을 알려주는 메시지를 프롬프트 메시지라 한다.
	 * 
	 * 특히 키보드에서의 입력 시 프롬프트 메시지는 중요하다.  
	 */
	
	// 프롬프트 메시지와 문자 입력
	public static char getCharWithPrompt(String msg) {
		System.out.print(msg);
        return getChar();
	}

	public static char getChar(String prompt) {	
		return getCharWithPrompt(prompt);
	}
	
	// 프롬프트 메시지와 int 정수 입력
	public static int getIntWithPrompt(String msg) {
		System.out.print(msg);
		return getInt();
	}
	
	public static int getInt(String prompt) {
		return getIntWithPrompt(prompt);
	}

	// 프롬프트 메시지와 long 정수 입력
	public static long getLongWithPrompt(String msg) {
		System.out.print(msg);
		return getLong();
	}
	
	public static long getLong(String prompt) {
		return getLongWithPrompt(prompt);
	}

	// 프롬프트 메시지와 double 실수 입력
	public static double getDoubleWithPrompt(String msg) {
		System.out.print(msg);
		return getDouble();
	}

	public static double getDouble(String prompt) {		
		return getDoubleWithPrompt(prompt);
	}

	
	// 프롬프트 메시지와 float 실수 입력
	public static float getFloatWithPrompt(String msg) {
		System.out.print(msg);
		return getFloat();
	}
	
	public static float getFloat(String prompt) {		
		return getFloatWithPrompt(prompt);
	}
	
	// 프롬프트 메시지와 boolean 값 입력
	public static boolean getBooleanWithPrompt(String msg) {
		System.out.print(msg);
		return getBoolean();
	}
	
	public static boolean getBoolean(String prompt) {		
		return getBooleanWithPrompt(prompt);
	}
	
	// 프롬프트 메시지와 문자열 입력
	public static String getStringWithPrompt(String msg) {
		System.out.print(msg);
		return getString();
	}
	
	public static String getString(String prompt) {
		return getStringWithPrompt(prompt);
	}
	
	// 프롬프트 메시지와 식별자 입력
	public static String getIdentifierWithPrompt(String msg) {
		System.out.print(msg);
		return getIdentifier();
	}
	
	public static String getIdentifier(String prompt) {
		return getIdentifierWithPrompt(prompt);
	}
	
	

	public static void main(String[] args) {	
		while(true) {

			/*
			 *  이 부분은 테스트를 위한 부분임
			 *  
			 *  입력 메소드를 한번씩 호출함
			 */
			
//
//			System.out.print("\n  * input int (최대값: " + (0x7FFF_FFFF) +") > ");
//			int no = SkScanner.getInt(); 
//			System.out.println("  o int = " + no);
//
//			System.out.print("\n  * input long(최대값: " + (0x7FFF_FFFF_FFFF_FFFFL) +") > ");
//			long l = SkScanner.getLong(); 
//			System.out.println("  o long = " + l);
//
			System.out.print("\n  * input double > ");
			double  d = SkScanner.getDouble();
			System.out.println("  o double = " + d);
			System.out.print("\n  * input char > ");
			char c = SkScanner.getChar(); 
			System.out.println("  o char = "+ c +" (" + (int) c + ")");

			System.out.print("\n  * input String > ");
			String s = SkScanner.getString();
			System.out.println("  o String = '" + s + "'");  
	
			System.out.print("\n  * input identifier > ");
			String id = SkScanner.getIdentifier();
			System.out.println("  o identifier = " + id);  

			System.out.print("\n  * input boolean (true or false) > ");
			boolean b = SkScanner.getBoolean();
			System.out.println("  o boolean = " + b);  

			/*
			 * 프롬프트 메시지를 출력하는 입력 메소드 호출
			 */
			c = SkScanner.getCharWithPrompt("\n  * input char > "); 
			System.out.println("  o char = "+ c +" (" + (int) c + ")");

			int no = SkScanner.getIntWithPrompt("\n  * input int > "); 
			System.out.println("  o int = " + no);

			d = SkScanner.getDoubleWithPrompt("\n  * input double > ");
			System.out.println("  o double = " + d);

			s = SkScanner.getString("\n  * input String > ");
			System.out.println("  o String = '" + s + "'");  
	
			id = SkScanner.getIdentifier("\n  * input identifier > ");
			System.out.println("  o identifier = " + id);  

			b = SkScanner.getBoolean("\n  * input boolean (true or false) > ");
			System.out.println("  o boolean = " + b);  
			
			char c1=0;
			System.out.println("  * Hit any char and hit Enter key!!" );  
			while(c1 != '\n') {
				c1=getchar();

				System.out.print((int)c1 + " ");
			}
			
			System.out.print("End");
		}
	}
}

