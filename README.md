# Imstagram

1. 개발 환경 (개발 환경의 OS, IDE, 사용한 오픈소스 정보 등)
 - jdk1.8
 - Android Studio 2.2.3
 - 오픈소스(Download Images From Web And Lazy Load In ListView - Android Example)

2. 실행 환경 (테스트한 단말 모델 + OS 등의 정보 or 에뮬레이터 + OS 정보 등)
 - 갤럭시 S4 (4.4.2)

3. 실행 방법
  1. Android Studio에서 VCS -> Check out project from Version Control -> GitHub 메뉴를 선택한다.
  2. Clone Repository 창에 "https://github.com/oo2shipo/Imstagram.git" 주소를 입력해서 프로젝트를 가져온다.
  3. Run -> Debug 'app' 메뉴를 실행해서 실행한다.

4. 이슈 사항
  - 이미지 로딩의 기능 개선에 중점을 두고 개발을 진행했습니다.
    1. 이슈사항
      * ListView를 scroll을 하는 동안에 다수의 ImageView에 이미지 로딩 과정이 필요하다.
      * 이 과정에서 Heap Memory 부족으로 runtime 에러가 발생한다.
    2. 해결방안
      * ThreadPool 을 이용해서 thread를 재사용함 
      * Bitmap의 inSampleSize 옵션을 적절히 설정하여 rescale 하여서 Memory 사용을 감소시킴
  - 기타 API을 지원하기 위해서 ExtractFactory를 구현했습니다.
