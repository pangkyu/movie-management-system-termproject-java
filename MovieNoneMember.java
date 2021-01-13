// 비회원 에 관련된 데이터 모음
public class MovieNoneMember {
	private String name2; // 회원 이름
	private int phoneNum2; // 핸드폰 번호
	private int movienamenum2; // 영화번호
	private String moviename2; // 영화제목
	private int countHuman2; // 인원수
	private int paymentMethod2; // 결제 방법

	public MovieNoneMember() {

	}

	public MovieNoneMember(String name2, int phoneNum2, int movienamenum2, int countHuman2, int paymentMethod2) {
		this.name2 = name2;
		this.movienamenum2 = movienamenum2;
		this.countHuman2 = countHuman2;
		this.paymentMethod2 = paymentMethod2;
		this.phoneNum2 = phoneNum2;
	}

	public MovieNoneMember(String name2, int phoneNum2, int movienamenum2, int countHuman2) {
		this.name2 = name2;
		this.movienamenum2 = movienamenum2;
		this.countHuman2 = countHuman2;
		this.phoneNum2 = phoneNum2;
	}

	public MovieNoneMember(String name2, int phoneNum2, String moviename2, int countHuman2) {
		this.name2 = name2;
		this.moviename2 = moviename2;
		this.countHuman2 = countHuman2;
		this.phoneNum2 = phoneNum2;
	}

	public String getName2() {
		return name2;
	}

	public void setName(String name2) {
		this.name2 = name2;
	}

	public int getphoneNum2() {
		return phoneNum2;
	}

	public void setphoneNum2(int phoneNum2) {
		this.phoneNum2 = phoneNum2;
	}

	public int getMoviename2() {
		return movienamenum2;
	}

	public int getCountHuman2() {
		return countHuman2;
	}

	public void setCountHuman2(int countHuman2) {
		this.countHuman2 = countHuman2;
	}

	public int getPaymentMethod2() {
		return paymentMethod2;
	}

	public String getPaymentMethodname2() {
		return paymentMethod2 == 1 ? "현장결제" : "선결제";
	}

	public void setPaymentMethod2(int paymentMethod2) {
		this.paymentMethod2 = paymentMethod2;
	}

	public String toString() {
		return " 이름 : " + name2 + ", " + " 핸드폰번호 :" + phoneNum2 + ", 예약한 영화 : " + moviename2 + ", 예약 인원 수 : "
				+ countHuman2 + "명, 결제방법 : " + getPaymentMethodname2();
	}

	public void output() {
		System.out.println(this.toString() + "\n");
	}
}