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