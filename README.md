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
- { RequestParam : input, Instance Value : key, input-value : value } 구조.
- Instance Value 에 Getter, Setter 메서드를 제대로 매핑하지 않을시 키 값이 잘못 설정될 수 있으니 유의.

## CH2. 회원관리 프로젝트
- 일반적인 앱애플리케이션의 계층구조 차용 ( 컨트롤러 - 서비스 - 리포지터리 -> 도메인 )
- 컨트롤러 : REST Request 매핑
- 서비스 : 비지니스 로직 객체 계층
- 레퍼지토리 : DB 접근 객체 계층 (CRUD)
- 도메인 : 도메인 지식 정보 계층 (회원, 주문, 쿠폰 등 개별 객체 생성 및 정보 관리방)
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

### Service
#### 조건 
- 중복회원 가입 불가

#### Class 구현
- 회원가입, 중복회원 체크
- 전체 회원 조회
- 회원 조회(ID)

> TIL 
> 1. 별도 로직이 들어가는 경우 항상 개별 메서드로 구현하는게 유리
> 2. java.util.List(Interface), java.awt.List(Class) 중 반환타입으로 설정할 때는 인터페이스를 사용. 클래스를 사용하는 경우 별도 오버로딩이 필요하다.

### Spring Test
1. Spring Test 의 경우 Junit Framework 를 사용하여 단위별 테스트 진행 가능
2. @Test Annotation 을 통해 Test 진행 method 표시
3. Assertion 클래스 사용으로 기대결과와 결과값 비교 진행
4. 개별 Method / Class / Package 단위로 테스트 진행 가능 (테스트 순서 보장 x)
5. package 단위로 테스트시 초기화작업 미진행시 저장장소에 중복데이터 존재하는 경우 오류 발생 가능
6. 즉 테스트 설계시 테스트 대상 간 의존관계가 없도록 설계 필요
7. 오류 발생 방지를 위한 @AfterEach Annotation 을 통해 매 테스트 후 저장장소 초기화
8. given, when, then 에 의해 테스트 코드를 짜는 게 편리 -> 어떤 조건하에 어떤 작업이 진행될 때 어떤 결과가 나와야함
9. Test 는 독립적으로 생성되어야 하기 때문에 @BeforeEach Annotation 을 통해 테스트 진행 전에 repository 를 새로 생성.
10. 테스트 기간에 사용되는 repository 는 서비스에서 사용되는 repository 와 동일함이 보장되어야 함으로 서비스 안에 별도 인스턴스 변수로 레포지터리를 생성하지 말고 외부에서 인자로 받아 생성할 수 있도록 생성자 추가(DI 작업)
11. DI 작업은 [ 1.서비스 생성자에 repository 를 입력 받을 수 있도록 설정 2. 테스트클래스 로컬 변수로 서비스와 레퍼지토리를 각각 선언하고 @BeforeEach Annotation 에서 레퍼지토리 생성 후 서비스에 주입 ] 하여 진행 

> TIL 
> TDD(Test Driven Development) : 테스트 설계를 먼저 진행한 후 실제 클래스 구현하는 방식으로 개발 진행 
> 여러 명이 동시 개발하는 상황에선 테스트 코드를 작성하는 편이 유리

## Ch4. 스프링 빈과 의존관계
- 스프링은 IoC 와 DI를 구현하기 위해서 스프링 컨테이너와 빈이라는 체계를 구축
- 스프링 컨테이너는 스프링이 관리해야하는 자바 객체를 스프링 환경설정의 빈으로 등록
- 스프링 빈은 스프링이 생명주기 및 추가적인 기능을 제공하는 자바객체(cf. 스프링 빈은 기본적으로 싱글톤으로 등록. 같은 스프링 빈의 경우 같은 인스턴스를 활용하여 메모리를 절약)
- 컴포넌트 스캔 혹은 환경설정에 빈을 등록함으로써 스프링 컨테이너의 IoC 개념 구현 및 DI 설정 가능
- 예제에는 생성자를 사용하였지만 setter 혹은 필드로도 DI 가능 (필드는 변경 불가능, setter 는 생성된 객체가 public 으로 노출된다는 단점이 있음)
- `@Autowired` 등록된 컴포넌트의 의존관계를 주입해주는 Annotation(단일 생성자의 경우 생략가능)

> *IoC란?*
> 1. 구현 객체는 자신의 로직을 실행하는 역할만 담당하고 프로그램의 제어 흐름은 외부 클래스(환경설정)가 담당
> 2. 각 로직에서 구현해야하는 객체는 App 의 환경설정에서 언제든 하위 구현 클래스를 변경하여 확장할 수 있음.

> > *DI란?*
> 1. 객체 간 의존 관계 주입을 의미
> 2. 객체 간 의존 관계 주입은 정적 객체 의존 관계와 동적 객체 의존 관계가 존재
> 3. 정적 객체 의존 관계는 MemberService 의 객체가 MemberRepository 객체에 의존하는 거처럼 주입된 객체를 표기하는 걸 의미
> 4. 동적 객체 의존 관계는 앱이 실행될 때 외부(SpringConfig)에서 의존 객체를 실제로 구현하고 클라이언트에 전달하여 클라이언트와 서버의 실제 의존 관계가 연결되는 걸 의미(예제에서는 MemberRepository 인터페이스 객체를 MemoryMemberRepository 로 구현)
> 5. 의존 관계를 주입하면 클라이언트 코드를 변경하지 않더라도 클라이언트가 사용하는 인스턴스 변경 가능

### 컴포넌트 스캔
- 컴포넌트란 `@Component` Annotation 으로 스프링 컨테이너가 관리해야하는 객체를 의미
- `@Controller`, `@Service`, `@Repository` 등은 모두 `@Component` 를 확장한 Annotation
- 컴포넌트 스캔이란 컴포넌트 Annotation 을 활용하여 환경설정에 별도로 빈 등록을 하지 않고도 IoC 컨테이너가 관리할 자바 객체를 지정
- 컴포넌트 스캔의 대상은 main 클래스에 import 된 패키지의 하위 패키지로 한정

### Bean 등록
- 메인 클래스에 import 된 패키지 안에 환경설정 클래스 생성 후 `@Configuration` Annotation 으로 관리되어야하는 객체를 메서드로 등록

## CH5. 웹 MVC 개발
### 회원 page 작성
- index page 의 경우 컨트롤러에 등록된 별도 페이지가 없다면 static resource 를 사용 
- 회원가입
  - 클라이언트가 회원가입을 요청하는 Post template 작성
  - Post template 의 <form> 태그 안 <input> 태그의 Attribute 는 Form class 에 전달되는 키 값으로 매핑
  - 회원가입을 신청할 수 있는 Page 로 이동할 수 있도록 컨트롤러 생성 후 작성된 템플릿에 매핑(String 반환 시 viewResolver 에서 자동 매핑)
  - Post template 의 입력 값을 컨트롤러에 전달할 수 있도록 Form class 작성(이름을 일치시키면 자동으로 매핑?)
  - Post 요청을 처리할 수 있는 Post Controller 작성
  - Post Controller 에서 Form 을 인자로 받아 새롭게 생성하는 domain 객체에 Form 의 저장된 값을 설정
  - 회원가입을 처리하는 Service 로 회원가입 완료
- 회원목록
  - 목록을 볼 수 있는 템플릿 작성
  - 타임리프 문법으로 `th:each` 로 루프를 생성하고 `th:text` 로 개별 객체를 받을 수 있음
  - 반환된 객체는 캡슐화 되어있기 때문에 접근은 property 접근(getter, setter method) 로 가능하다
  - 작성된 템플릿에 매핑된 Controller 생성 

## CH6. 스프링 DB 접근 기술
DB Config
1. DBMS : h2 (학습용)
2. channel : socket(tcp)
3. application.properties 설정
   1. DB 서버 URL
   2. Driver 설정
   3. 스프링부트 2.4부터는 유저네임 반드시 추가!

### JDBC
1. jdbc API 로 연결하는 Repository 클래스 구현
2. DB SQL 연결을 위한 3단계 구성 (network 커넥션 문제로 에러가 많이 발생하기 때문에 try catch 잘 넣어야하며 리소스 사용 후 close 를 잘 사용해서 사용된 자원을 반납해야함)
   1. DataSourceUtils.getConnection : DB 와 커넥션 생성 및 PreparedStatement 객체의 작성한 sql 문 전달. ex) conn.preparedStatement(sql, Statement.REUTRN_GENERATED_KEY[키 자동생성]) 
   2. PreParedStatement : 전달받은 sql 문에 values(?) 를 객체의 iv를 활용하여 셋팅 및 실행. 실행 후 객체의 조작이 필요하다면(DB 에서 키를 생성하는 로직으로 짰다면 멤버 객체에 전달해야하기 때문) 객체에 해당 정보 전달. ex) pstmt.setString(1, member.getName()) -> executeUpdate() -> rs = pstmt.getGeneratedKeys(); 
   3. ResultSet : 전달받은 DB 생성 정보를 객체에 저장
3. 메모리에 연결해둔 Repository 를 스프링 설정에서 DI 를 통해 DB에 연결하는 방식으로 변경(개방 폐쇄 원칙)

> TIL
> 개방-폐쇄 원칙(OCP, Open-Closed Principle) : 확장에는 열려있고, 수정, 변경에는 닫혀있다.
> 애플리케이션의 기능을 확장해도 확장된 기능을 조립하는 부분의 수정만 필요하지 애플리케이션을 동작하는 모든 코드를 수정할 필요가 없다.
> ex. 스프링 DI 를 활용하여 Repository 구현체의 DB 를 변경하더라도 해당 부분만 조립

### Spring Integration Test
1. 통합테스트를 위한 별도 서비스 클래스 생성(생성된 클래스는 `@SpringBootTest`, `@Transactional` Annotation 으로 관리)
2. 진행되는 테스트는 MemoryMember Repository 가 아닌 실제 존재하는 DB 에서 테스트 진행

> TIL
> @Transactional Annotation 은 테스트환경에서 DB CUD 작업이 있는 경우 테스트 후 자동으로 롤백하는 기능(정확히 얘기하면 아예 DB에 반영을 안함)을 제공
> 따라서 @Transactional 사용시 @AfterEach, @BeforeEach 사용 안해도 무방
> 테스트 환경에서 Commit 을 원하는 경우, 해당 기능에 `@Commit` 을 사용하면 됨
> SpringBootTest 의 경우에는 통합테스트를 사용하고 간단한 단위테스트 기능에는 로컬 자바 환경에서 진행하는 걸 지향
능

### JDBC Template
1. 기존 JDBC 의 redundant 부분들을 제거
2. 기존에 커넥션, SQL 문 수행, 결과 적용하던 모든 단계들이 RowMapper 로 통합
3. SimpleJdbcInsert 클래스를 사용하게 되면 별도 sql 문 작성 없이 pk 값과 속성 값을 활용하여 인서트 가능

> TIL
> 생성자 하나일 때 @Autowired 생략 가능 

### JPA
1. 자바진영의 표준 API 
2. 구현은 각 업체에서 진행
3. ORM(Obeject Relational Mapping)을 Annotation 을 활용하여 구현
4. JPA 를 사용하면 객체를 가지고 CRUD 를 위한 쿼리를 짤 필요가 없음(단, pk 기반 조회 이외의 쿼리 제외. 스프링데이터 jpa 의 경우에는 pk 기반 CRUD가 아니더라도 가능)
5. JPA 를 사용하려면 서비스 계층에도 Transactional Annotation 필요. 모든 데이터 변경은 Transactional 안에서 이루어져야함.

> TIL
> 서비스 환경에서의 @Transactional 은 데이터 작업을 정상적으로 DB 에 반영

### Spring Data JPA
JPA 로 구성한 repository 가 존재한다면 인터페이스의 상속만으로도 별도 repository 구현 없이 개발하는 기능

## CH7. AOP(Aspect oriented Programming)
시간측정과 같이 비지니스 핵심 로직이 아니지만 공통의 관심 사항인 로직들은 비지니스 로직과 섞여 개발했을 때 개발 효율성을 떨어트림.
이에 공통 관심 사항(cross-cutting concern)과 핵심 관심 사항(core-concern)을 분리하여 개발하는 개발방법론이 AOP.

1. AOP 클래스의 경우 특별한 상황에 사용하는 클래스임으로 컴포넌트 스캔보다는 빈으로 등록하여 별도로 관리
2. `@Around` Annotation 을 통해 패키지 단위에서부터 클래스 단위까지 지정하여 관리 가능


## keymap
1. command + shift + 방향키 : 줄바꿈
2. ctrl + shift + R : 열려있는 클래스 실행, 테스트 클래스 파일 실행시 커서가 존재하는 테스트 단위만 실행
3. command + option + v : (메서드, 클래스) 결과값 할당
4. command + N : getter and setter, equals and HashCode, toString, Override, Test 자동생성
5. ctrl + T : extract method, interface 등등 로직안에 존재하는 추가 로직을 별도로 추출
6. command + shift + T : Test 자동 생성
7. option + enter : lambda 변형
8. command + option + N : inline code 로 리팩토링