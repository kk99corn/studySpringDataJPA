# studySpringDataJPA
## 스프링 데이터 JPA 스터디
- https://www.inflearn.com/course/스프링-데이터-JPA-실전
- 기간: 2022.08.14 ~
- 프로젝트 구성
	- Java 17
	- SpringBoot 2.7.2
  - Dependencies
    - Spring Web
    - Spring Data JPA
    - H2 Database
    - Lombok
 
## Study Notes 
### 08.14 - 1일차 
 - @Transactional: JPA CUD는 무조건 트랜잭션안에서 실행되야함.
 - 동일한 Transaction 안에서는 영속성 컨텐츠들의 동일성을 보장함.
 - Lombok
    - @Setter: 실무에서 가급적 Entity에 Setter는 사용하지 않기
    - @NoArgsConstructor AccessLevel.PROTECTED: 기본 생성자 막고 싶은데, JPA 스팩상 PROTECTED로 열어두어야 함
    - @ToString은 가급적 내부 필드만(연관관계 없는 필드만)
 - JPA
    - @ManyToOne fetch타입 LAZY(지연로딩)으로 변경(default: EAGER(즉시로딩))
      - fetch타입 EAGER(즉시로딩)으로 사용시 실무에서 성능최적화 힘듦
    - JPQL: Java Persistence Query Language
      - JPQL은 엔티티 객체를 대상으로 쿼리를 질의
 - Optional.ofNullable: 값이 null일수도있다.


 - Spring Data JPA
    - interface만 선언하면 동작하는 이유
        - interface(JpaRepository를 extends하는)만 보고 spring data jpa가 구현체(proxy 객체)를 만들어서 꽂아줌
        - https://velog.io/@kshired/Spring-%EC%99%9C-JPARepository%EB%8A%94-Repository%EA%B0%80-%ED%95%84%EC%9A%94-%EC%97%86%EC%9D%84%EA%B9%8C-deep-dive-%ED%95%B4%EB%B3%B4%EA%B8%B0
            1. JpaRepositry 를 impl 하는 클래스는 EnableJpaRepositories + JpaRepositoriesRegisterar에 의해 자동으로 빈으로 등록된다.
            2. JpaRepository 인터페이스는 @NoRepositoryBean 어노테이션에 의해 인터페이스 그 자체가 빈으로 등록될 수 없게 설정해준다.
            3. NoRepositoryBean 을 설정하면, 여러 과정을 거쳐 excludeFilters를 이용하여 NoRepositoryBean 어노테이션이 달린 애들을 알아서 걸러준다.
    - 쿼리 메소드 필터 조건
        - Spring Data JPA 공식 문서: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    - 쿼리 메소드 기능
        1. 메소드 이름으로 쿼리 생성
            - 메소드 이름을 분석해서 JPQL 쿼리 실행
                - 조회: find…By ,read…By ,query…By get…By
                - COUNT: count…By 반환타입 long
                - EXISTS: exists…By 반환타입 boolean 
                - 삭제: delete…By, remove…By 반환타입 long
                - DISTINCT: findDistinct, findMemberDistinctBy
                - LIMIT: findFirst3, findFirst, findTop, findTop3
        2. 메소드 이름으로 JPA @NamedQuery 호출
            - 컴파일시점에 Query 구문오류를 파악하는 장점이 있음
            - but, 잘 안씀.. why? repository @Query가 더 강력 
        3. @Query 어노테이션을 사용해서 repository 인터페이스에 쿼리 직접 정의
            - @NamedQuery와 동일하게 컴파일시점에서 Query 구문오류를 파악 가능하다.
        4. 파라미터 바인딩
        5. 반환 타입
        6. 페이징과 정렬
        7. 벌크성 수정 쿼리
        8. @EntityGraph