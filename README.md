# 스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술

__Environment__
- Language : Java ( jdk 17 )
- IDE : IntelliJ
- Backend Framework : Spring Boot 2.7.4
- Dependencies : Spring Web / Thymeleaf

This project created by Spring Initializr. Visit https://start.spring.io/ for further information.

## CH1. 스프링 웹개발 기초
> XML Schema : Thymeleaf
> 
> This project uses Thymeleaf as a template engine which supports [ th ] grammar in addition to general html tags. 

### 정적컨텐츠
- static contents : 모델을 통한 binding parameter 없이 작성된 html 파일을 그대로 출력.
- Web browser 에서 REST 호출 시, 사용된 url을 매핑한 controller 존재여부 선탐색.
- 매핑된 controller 존재여부에 따라 1. controller 매핑 혹은 2.static 폴더탐색 결정 후 template 출력.

### MVC & template engine
- 동적 바인딩 변수 활용
- @RequestParams 를 활용하여 바인딩변수 할당 가능. 
- Tomcat : REST 호출시 사용된 url 정보를 활용하여 controller 에 매핑.
- Controller : html parmater(ex. ?name=ex) 존재시 모델 클래스에 입력. 
- Model : 입력된 인자를 모델 Attribute 에 추가 후 viewResolver 에 전달.
- View : Model 에서 전달받은 인자를 template 에 매핑하여 출력.

> TIL : Thymeleaf 의 장점 중 하나는 절대경로 복사시 동적으로 바인딩된 html 파일이라 하더라도 html 파일을 웹에서 확인가능
 
### API
- http 통신을 통해 받은 입력 데이터를 그대로 html에 렌더링.
- 별도 템플릿을 통한 렌더링 x.
- 객체 반환시 HttpMessageConverter 가 http 통신을 통해 전달 받은 parameter 를 키값으로 json 데이터 출력.
- { RequestParam : input, parameter or Instance Value : key, input-value : value } 구조.

## 회원관리 프로젝트
- 일반적인 앱애플리케이션의 계층구조 차용 ( 컨트롤러 - 서비스 - 리포지터리 -> 도메인 )
- 컨트롤러 : REST Request 매핑
- 서비스 : 비지니스 로직 객체 계층
- 레퍼지토리 : DB 접근 객체 계층
- 도메인 : 도메인 지식 정보 계층 (회원, 주문, 쿠폰 등)
- JPA, RDBMS 구조를 정하지 않아 interface 로 구현

### Repository
#### Interface 구성
- 회원저장(C) : Member save(Member member).
- 회원조회(R) : Optional findById(Long id). null type 반환을 고려한 Optional 클래스 사용
- 회원조회(R) : Optional findByName(String name). null type 반환을 고려한 Optional 클래스 사용
- 회원조회(R) : List findAll().

#### Class 구현
- Hashmap<Id, Member> 저장공간 생성. 실무에서는 동시성을 고려하여 ConcurrentHashmap 사용
- ID로 사용할 Long 타입 시퀀스 생성. 실무에서는 동시성을 고려하여 AtomicLong 사용.

