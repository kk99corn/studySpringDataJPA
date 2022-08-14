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
 - Entity + @Setter: 기본적으로 Entity에 필드 Setter 추천하지 않음.
 - @Transactional: JPA CUD는 무조건 트랜잭션안에서 실행되야함.
 - 동일한 Transaction 안에서는 영속성 컨텐츠들의 동일성을 보장함.