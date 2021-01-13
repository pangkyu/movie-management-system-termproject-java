import java.util.Vector;

class Movie {

   String title, genre, director; // 제목, 장르, 감독
   String date, theater; // 상영날짜, 상영관

   Movie() { // 다른 객체 생성자를 작성할 경우 매개변수 없는 객체 생성자를 작성하는 것이 좋음
   }

   // 제목, 장르, 감독, 상영관, 상영날짜가 주어지면 영화 객체 생성하는 객체 생성자
   Movie(String title, String genre, String director, String theater, String date) {
      this.title = title;
      this.genre = genre;
      this.director = director;
      this.theater = theater;
      this.date = date;
   }

   // 영화의 제목, 장르, 감독, 상영관, 상영날짜을 입력하여 필드에 저장하는 입력을 위한 메소드
   void input() {
      this.title = SkScanner.getStringWithPrompt("  o 영화제목 > ");
      this.genre = SkScanner.getStringWithPrompt("  o 장르 > ");
      this.director = SkScanner.getStringWithPrompt("  o 감독 > ");
      this.theater = SkScanner.getStringWithPrompt("  o 상영관 > ");
      this.date = SkScanner.getStringWithPrompt("  o 상영날짜 > ");
   }

   // 객체의 필드 값들을 문자열로 만들어 반환하는 메소드.
   public String toString() {
      return "제목: '" + this.title + "',  장르: '" + this.genre + "',  감독: '" + this.director + "', 상영관: '"
            + this.theater + "', 상영날짜: '" + this.date + "'";
   }

   // 객체의 필드 값들 출력하기 위한 메소드
   // toString() 메소드를 이용하여 처리
   void output(String message) {
      System.out.println("  " + message + " - " + this.toString());
   }

   public String toStringTitle() {
      return "제목: '" + this.title + "'";
   }

   void outputTitle(String message) {
      System.out.println("  " + message + " - " + this.toStringTitle());
   }
}