# **도서훼손 판별 프로젝트**
<img src="https://github.com/user-attachments/assets/b67cf068-bdc0-48e4-aa53-c522824307a7" alt ="무인파손시스템" width = "600" height="400">

## 프로젝트 소개
- AI가 발전함에 따라 많은 곳에서 **자동화**가 진행되고 있으며, 이에 따라 자동으로 **도서의 파손 상태를 확인**
- 사서의 **업무 부담**과 인력난을 **해소**하기 위해  **AI**활용하여 도서 대여 전,후의 **파손상태를 감지**하여 판정을 내려주는 시스템 구현
</br>

## 팀원 구성 & 역할
<div align="center">


| **김수겸** | **박성목** | **이지은** | **조형석** |
| :------: |  :------: | :------: | :------: |
| <img src="https://github.com/user-attachments/assets/e553fca2-ffae-421f-8e77-bed2e1a7bf32" height=150 width=150> <br/>  **AI모델링** | <img src="https://github.com/user-attachments/assets/c1d237ad-1685-44a5-b476-b0da86dcdaef" height=150 width=150> <br/> **AI모델링** |<img src="https://github.com/user-attachments/assets/0ce22f69-4444-4923-85c2-896217f7dcc5" height=150 width=150> <br/> **Android**| <img src="https://github.com/user-attachments/assets/5abd2834-0221-477f-a75e-ee580d22c1f0" height=150 width=150> <br/> **Android**|

</div>
<br>

## 개발 구조
<div align="center">
  <img src="https://github.com/user-attachments/assets/796de08d-aec1-4619-9aae-5dabddd34f00" alt="제목을 입력해주세요" width="700" height="300">
</div>

## 개발 환경:
- Front : Android Studio JAVA (target sdk 32 / mid sdk 21)
- DB :  Firebase Storage
- AI : Python (Yolov5)

## 개발 기간 
- 전체 개발 기간 : 2022-10-05 ~ 2022-11-05
- 회의 :  2022-10-11 ~ 2022-10-29 ((주)디월드 전무이사님 멘토링) 
- 기능 구현 : 2022-10-10 ~ 2022-11-04

## 모델링 
- 데이터셋 구성
![image](https://github.com/user-attachments/assets/4b1ee16b-39b2-44a1-9ce0-89de0fd1661d)
![image](https://github.com/user-attachments/assets/8048473c-87e8-4caf-90ea-1a5e308e768c)

## 학습코드 
https://github.com/cho123456789/Hustar-BMK-Yolov5

## 패이지별 기능

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/d4ed2685-19f4-49de-9257-f39e5ea26b43" alt="splash" width="300"/></td>
    <td>
      <h3>초기화면</h3>
      <ul>
        <li>splash 화면이 잠시 나온 뒤 다음 페이지가 나타납니다.</li>
        <li>사용자 정보를 넣고 <b>책반납버튼</b>을 누르면 반납이 시작됩니다</li>
        <li>로그인 기능은 구현되어 있지 않습니다.</li>
      </ul>
    </td>
  </tr>
</table>


<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/8cf62ff1-6661-40b6-9690-ecaafc93123c" alt="splash" width="300"/></td>
    <td>
      <h3>바코드스캔</h3>
      <ul>
        <li>카메라 촬영버튼을 통해서 책에 있는 바코드를 스캔합니다</li>
        <li><b>업로드 버튼</b>을 통해 바코드에 있는 책에 대한 정보를 불러옵니다</li>
        <li>바코드 인식 개선 문제로 숫자로 대체하였습니다.</li>
      </ul>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/20b6d594-4bba-44a3-936a-4a5c99cf281e" alt="splash" width="303"/></td>
    <td>
      <h3>책 촬영</h3>
      <ul>
        <li>카메라 촬영버튼을 통해서 책을 스캔합니다.</li>
        <li><b>업로드 버튼</b>을 통해서 책의 이미지정보를 전송합니다.</li>
        <li>이미지 정보를 학습이 완료된 Yolov5모델에 테스트를 진행합니다.</li>
      </ul>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/d68bca12-193f-413e-875a-081f070f8fa9" alt="splash" width="303"/></td>
    <td>
      <h3>책 훼손여부 파악</h3>
      <ul>
        <li>책 이미지를 통해서 훼손 여부에 대한 이미지 정보를 불러옵니다.</li>
        <li><b>줌인기능</b>을 토대로 훼손여부를 파악합니다.</li>
      </ul>
    </td>
  </tr>
</table>

## 트러블 슈팅 

- **학습 정확도 개선** <br>
      데이터 클래스 불균형으로 정확도가 낮아지는 현상 발생 (찢어짐 ⇒ 긁힘으로 표시) <br>
    => (roboflow 사이트를 이용하여 데이터 증강  및 라벨링을 통해서 데이터 클리닝 실시) <br>
    ⇒ (찢어짐 : 316 개 , 얼룩 394개  닳음 : 670개 , 긁힘 282개  데이터 셋 확보) 

- **이미지 데이터 처리** <br>
    사진 데이터가 저화질로 출력되어 디텍팅이 원활하지 않은 현상이 발생 <br>
     ⇒   (안드로이드에서 Bitmap 통해서 사진 화질 개선) <br>
     ⇒  (사용자가 보기 싶게  Zoom in 기능을 추가하여 훼손 데이터 확인)

## 개발툴
<img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat-square&logo=Android Studio&logoColor=white"/> <img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=white"/>
