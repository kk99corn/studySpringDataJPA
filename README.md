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
---------------
## Study Notes 
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
            - 주의! DTO로 직접 조회 하려면 JPA의 new 명령어를 사용해야 한다. 그리고 다음과 같이 생성자가 맞는
              DTO가 필요하다. (JPA와 사용방식이 동일하다.)
        4. 파라미터 바인딩
            - 위치 기반(가급적 실무에서 사용하지 않아야한다.)
            - 이름 기반(추천)
        5. 반환 타입
            - 반환타입 컬렉션인 경우 주의점: 조회되는 값이 없어도 Empty Collection으로 반환됨
        6. 페이징과 정렬
            - Page: 추가 count 쿼리 결과를 포함하는 페이징
              - @Query -> countQuery를 재정의하여, 성능 최적화 가능
            - Slice: 추가 count 쿼리 없이 다음 페이지만 확인 가능(내부적으로 limit + 1조회)
            - List(자바 컬렉션): 추가 count 쿼리 없이 결과만 반환
        7. 벌크성 수정 쿼리
            - 주의!: 벌크연산하는 경우, DB에 다이렉트로 값이 변경되나, 영속성 컨텍스트 데이터는 수정이 안됨.
                1. 벌크연산 이후 영속성 컨텍스트를 다 날려줘야함(초기화)(EntityManager.clear())
                2. @Modifying(clearAutomatically = true) 자동으로 영속성 컨텍스트 클리어
            - spring data jpa
                - @Modifying: update 쿼리에선 modifying 넣어줘야한다.
        8. @EntityGraph
            - fetch join? @Query("select m from Member m left join fetch m.team")
                - 연관된(관계가 있는) 데이터를 한번에 다 조회
            - @EntityGraph(attributePaths = {"team"})
                - EntityGraph는 fetch join을 쉽게 해준다.
    - JPA Hint
        - JPA 쿼리 힌트(SQL 힌트가 아니라 JPA 구현체(예시: hibernate)에게 제공하는 힌트)
            - 실무에서 잘 사용하지않음.
    - JPA Lock
        - select ... for update
    - 확장 기능
        - 순수 JPA - 주요 이벤트 어노테이션
            - @PrePersist
            - @PostPersist
            - @PreUpdate
            - @PostUpdate
        - Spring Data JPA
            - @EnableJpaAuditing 필수로 메인 클래스에 선언
    - Web 확장
        - 페이징
            - 예) /members?page=0&size=3&sort=id,desc&sort=username,desc
            - page: 현재 페이지, 0부터 시작한다.
            - size: 한 페이지에 노출할 데이터 건수
            - sort: 정렬 조건을 정의한다. 예) 정렬 속성,정렬 속성...(ASC | DESC), 정렬 방향을 변경하고 싶으면 sort
            - 파라미터 추가 (asc 생략 가능)
        - application.yml에서 global 설정 변경 가능
        - @PageableDefault()로 지역 설정 변경 가능(global보다 우선 적용)


   - Spring Data JPA 구현체 분석
        - @Repository 적용: JPA 예외를 스프링이 추상화한 예외로 변환
        - @Transactional 트랜잭션 적용
            - JPA의 모든 변경은 트랜잭션 안에서 동작
            - 스프링 데이터 JPA는 변경(등록, 수정, 삭제) 메서드를 트랜잭션 처리 
            - 서비스 계층에서 트랜잭션을 시작하지 않으면 리파지토리에서 트랜잭션 시작
            - 서비스 계층에서 트랜잭션을 시작하면 리파지토리는 해당 트랜잭션을 전파 받아서 사용
            - 그래서 스프링 데이터 JPA를 사용할 때 트랜잭션이 없어도 데이터 등록, 변경이 가능했음(사실은
            - 트랜잭션이 리포지토리 계층에 걸려있는 것임)
        - @Transactional(readOnly = true)
            - 데이터를 단순히 조회만 하고 변경하지 않는 트랜잭션에서 readOnly = true 옵션을 사용하면 플러시를 생략해서 약간의 성능 향상을 얻을 수 있음
        - ** save() 메서드
            - 새로운 엔티티면 저장( persist )
            - 새로운 엔티티가 아니면 병합( merge )


   - 새로운 Entity를 구별하는 방법
        - JPA 식별자 생성 전략이 @GenerateValue 면 save() 호출 시점에 식별자가 없으므로 새로운 엔티티로 인식해서 정상 동작한다. 
        - 그런데 JPA 식별자 생성 전략이 @Id 만 사용해서 직접 할당이면 이미 식별자 값이 있는 상태로 save() 를 호출한다. 
          따라서 이 경우 merge() 가 호출된다. merge() 는 우선 DB를 호출해서 값을 확인하고, DB에 값이 없으면 새로운 엔티티로 인지하므로 매우 비효율 적이다. 
          따라서 Persistable 를 사용해서 새로운 엔티티 확인 여부를 직접 구현하게는 효과적이다.
        - 참고로 등록시간( @CreatedDate )을 조합해서 사용하면 이 필드로 새로운 엔티티 여부를 편리하게 확인할 수 있다. (@CreatedDate에 값이 없으면 새로운 엔티티로 판단)


   - 나머지 기능들
        - Specifications(명세) - 실무에서 잘 안씀
            - 책 도메인 주도 설계(Domain Driven Design)는 SPECIFICATION(명세)라는 개념을 소개 
            - 스프링 데이터 JPA는 JPA Criteria를 활용해서 이 개념을 사용할 수 있도록 지원
        - Query By Example - 실무에서 잘 안씀
        - Projections
            - Entity 대신에 DTO를 편하게 조회하기 위해 사용
            - 전체 엔티티가 아니라 만약 회원 이름만 딱 조회하고 싶으면
            - interface 기반
            - class 기반
                - 생성자 파라미터명이 똑같아야함
            - 주의
                - 프로젝션 대상이 root 엔티티면, JPQL SELECT 절 최적화 가능
                - 프로젝션 대상이 ROOT가 아니면
                    - LEFT OUTER JOIN 처리
                    - 모든 필드를 SELECT해서 엔티티로 조회한 다음에 계산 
            - 정리
                - 프로젝션 대상이 root 엔티티면 유용하다.
                - 프로젝션 대상이 root 엔티티를 넘어가면 JPQL SELECT 최적화가 안된다!
                - 실무의 복잡한 쿼리를 해결하기에는 한계가 있다.
                - 실무에서는 단순할 때만 사용하고, 조금만 복잡해지면 QueryDSL을 사용하자
        - Native Query
            - 가급적 네이티브 쿼리는 사용하지 않는게 좋음, 정말 어쩔 수 없을 때 사용..
            - @Query(value = "select * from member where username = ?", nativeQuery = true)
    