
## **영화 관련 총 2가지 간단한 프로젝트로 구성**

**1.**

## 🎈 **영화 검색 기능 제공 앱 (Film Finder)**
> ######    • (폴더명: k_bindingAdapter_RecyclerView3) (Kotlin)
> ######    • 검색창으로부터 영화 검색이 가능합니다.
> ######    • 영화 선택 시 포스터, 평점, 출시일, 줄거리 등을 제공해줍니다.


✔ **프로젝트를 진행한 계기**

1. ViewModel, DataBinding 공부를 하며 RecyclerView에 DataBinding을 적용해 보고자 하여 간단하게 만들어 본 프로젝트

✔ **오픈 API 제공**

The Movie DB (https://developers.themoviedb.org/3/getting-started/introduction)

✔ **기술**

📌 ViewModel, DataBinding, BindingAdapter

XML에 데이터를 지정해주어서 직접 데이터 값을 적용해주지 않아도 ViewModel을 통해 데이터를 전달해줍니다.
BindingAdapter를 통해 원하는 기능의 메소드를 만들어 적용해줄 수 있습니다.

📌 Retrofit

TMDB에서 제공해주는 오픈 API에서 데이터를 가져오기 위한 통신을 담당합니다.

📌 RecyclerView

✔ **느낀점**

AAC에서 제공하는 여러 라이브러리가 최근 개발에 트렌드라는 점을 깨닫게 되었다.  
코드를 항상 findViewById 를 통해 데이터를 직접 지정해주고 하였는데, 바인딩을 사용한 방법이 클래스 숫자는 많아지지만 무언가 체계적이고 각자의 역할을 볼 수 있었던 것 같다.  
신기했다.  
아직 내가 모르는 것이 너무 많다.   
많이 부족하지만, 찾아보고 계속 이해해보려고 노력해야겠다.  

✔ **관련 이미지**

🚩 **로딩 화면 및 검색 화면**

![image](https://user-images.githubusercontent.com/61875571/112421786-16e82480-8d73-11eb-90ac-26fd113c212d.png)


🚩 **영화 검색 시**

![image](https://user-images.githubusercontent.com/61875571/112421865-4139e200-8d73-11eb-8b44-a91c287f3edd.png)


🚩 **영화 아이템 클릭 시 영화 정보**

![image](https://user-images.githubusercontent.com/61875571/112421977-7b0ae880-8d73-11eb-9c1f-f744d8025a86.png)


**2.** 

✔ **제목: 영화 정보 및 순위 제공 앱 (영순이(위))** (폴더명: project1_searchmovie) (Java)

카테고리 별 Rank 1 ~ 10 까지 영화들을 나열하여 보여주며, 영화에 대한 간단한 정보를 보여줍니다.

담기기능을 통해 내가 원하는 영화를 저장할 수 있습니다. (최대 300개까지 담기가 가능합니다.)

✔ **프로젝트를 진행한 계기**

1. 안드로이드 공부를 하며 오픈 API를 처음 사용해 본 후 무엇이라도 만들보고자 하는 마음으로 시작
2. 당시, 데이터를 가져와 사용자들에게 보여줄 수 있다는 개념이 매우 흥미⬆

✔ **오픈 API 제공**

영화진흥위원회(https://www.kobis.or.kr/kobisopenapi/homepg/main/main.do)

✔ **기술**

📌 Volley

영화진흥위원회에서 제공하는 오픈 API 데이터들을 가지고 와서 보여줍니다.
+ JSON

📌 SQLite

원하는 영화를 담기 기능을 통해 저장할 수 있습니다.

📌 RecyclerView

✔ **관련 이미지**

🚩 **첫 화면, 카테고리 별 영화 순위**

![image](https://user-images.githubusercontent.com/61875571/109920859-47055000-7cfe-11eb-8546-82ebdb378089.png)


🚩 **간단한 정보, 상세보기 누를 시 페이지 이동**

![image](https://user-images.githubusercontent.com/61875571/109920933-57b5c600-7cfe-11eb-9f02-e7880d558aad.png)


🚩 **담기 기능**

![image](https://user-images.githubusercontent.com/61875571/109921042-859b0a80-7cfe-11eb-9ded-7ae4802b090a.png)


🚩 **저장소, 담기 삭제**

![image](https://user-images.githubusercontent.com/61875571/109921126-b54a1280-7cfe-11eb-89d4-4359d18eebcf.png)

