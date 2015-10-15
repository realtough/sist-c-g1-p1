<h1> 무기한 연기 </h1>
- 조원 역할- <br>
riovalto - 조병훈 : 소켓통신 서버, 클라이언트 기본틀 완성, 업무총괄<br>
sirasonyboss - 김재호 : DB관련 DAO, VO, 로그인 서버, 클라이언트 점검 및 마무리<br>
kimwlgus1 - 김지현 : 채팅 서버, 클라이언트, 서버폼 점검 및 마무리<br>
ssanta69@naver.com - 주형준 : 그림맞추기 완성<br>
sleekwook - 조성욱 : 캐치마인드 완성<br>
<br>
<br>
- 참고 사항 -<br>
모든 게임은 800 x 600 크기의 JPanel 안에 담을것 (800x600으로 변경합니다)<br>
Commit시 작업 내역 코멘트 꼼꼼히 작성 합시다<br>
<br>
패키지분류<br>
com.sist.client : 클라이언트 (클라이언트는 모두 이쪽으로)<br>
com.sist.common : DAO, VO및 Tools클래스<br>
com.sist.server : 채팅, 로그인 서버와 폼<br>
<br>
파일별 설명<br>
<b>클라이언트</b><br>
LobbyMain : 메인 채팅 로비 (JFrame) 기본적으로 로그인한 모두와 대화<br>
LobbyLogin : 로그인 (Jdialog) 채팅 시작을 위해 메뉴에서 로그인을 눌러 띄움<br>
LobbyRegister : 가입창 (Jdialog). 로그인화면에서 가입 버튼 눌러 들어감<br>
LobbyAvatar : 아바타 선택창. 미완성. 버튼에 그림만 표시<br>
G1Client : 내용없고 기능없는 그냥 인터페이스 타입 파일. 아직 용도미정<br>
<br>
<b>Common</b><br>
UserInfoDAO : DB연동 작업 총괄 클래스<br>
UserInfoVO : 유저의 모든 속성값을 가지는 VO클래스<br>
Tools : 서버 아이피 포트등의 스태틱 변수값과 몇가지 필요한 메소드를 모아놓은 클래스<br>
<br>
<b>서버</b><br>
G1Server : 내용없고 기능없는 인터페이스. 역시 용도미정<br>
LoginServer : 소켓통신하는 로그인 서버 <br>
MainServer : 채팅 담당 메인서버<br>
ServerForm : 서버관련 내용 출력하는 JFrame폼<br>
UserListForm : 전체 유저목록 표시할 패널. 아직 폼만 있슴