스마트 세탁 어플리케이션 (세탁이)


본 애플리케이션은 세탁물 배달 중계 서비스 플랫폼입니다.


앱은 총 3개로 User = 일반 사용자, Onwer = 가게 운영자, Rider = 배달원 으로 구성됩니다.


프로젝트를 진행한 계기
1. 1인 가구가 늘어나고 바쁜 현대인의 삶에서 세탁이란 부분에서 시간을 조금이라도 아끼고 신경을 덜어주기 위함
2. 점차 사라져가는 소상 세탁소들의 매출과 홍보 역량을 증대
3. 배달 산업 활성화
4. 휴대폰 사용이 당연한 시대에 사용자들을 위한 서비스 플랫폼을 만들어보고 싶은 마음!!



주요 기술

MySQL

앱의 모든 데이터를 저장하는 공간으로 MySQL을 사용하였습니다. 무료인데다 다루기 쉬운 장점이 있어서 선정하게 되었고 PHP를 통해 앱과 통신하여 데이터를 주고 받는 역할을 합니다.


PHP(JSON)

PHP문을 통해 웹에 JSON 형식으로 데이터를 뿌려주어 앱에서 데이터를 주고 받을 수 있도록 해줍니다.


Volley

성능은 HttpURLConnection과 동일하지만, 큐만 추가하면 스레드 관리를 생략해주고 통신할 수 있고 코드가 훨씬 간결해져서 사용 


HttpURLConnection

구현하는데 참고하면서 만드느라 RecyclerView로 표현해주는 부분들은 AsyscTask로 UI, Thread를 관리하며 Http통신으로 구현


AsyncTask

스레드 통신 제어


commons-net-3.7

서버에 저장된 이미지 파일을 가져오기 위해 FTPClient 사용


PHPMailer

사용자들에게 필요한 경우 메일을 송신하기 위해 사용


MessageDigest

비밀번호 해시화를 위해 사용


Fragment

화면 재사용을 위해 User의 가게 메인과 리뷰를 보는 곳에 ViewPager와 Rider 메인 Tab 형식 구현


