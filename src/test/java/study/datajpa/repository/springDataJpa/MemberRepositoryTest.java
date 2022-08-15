package study.datajpa.repository.springDataJpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.springDataJpa.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	TeamRepository teamRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Test
	public void testMember() {

		Member member = new Member("memberA");
		Member savedMember = memberRepository.save(member);

		Optional<Member> byId = memberRepository.findById(savedMember.getId());
		Member findMember = byId.get();

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

		/*
		 * findMember == member
		 * 동일한 Transaction 안에서는 영속성 컨텐츠들의 동일성을 보장함
		 */
		assertThat(findMember).isEqualTo(member);
	}

	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");
		memberRepository.save(member1);
		memberRepository.save(member2);

		// 단건 조회 검증
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		// 리스트 조회 검증
		List<Member> all = memberRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		// 카운트 검증
		Long count = memberRepository.count();
		assertThat(count).isEqualTo(2);

		// 삭제 검증
		memberRepository.delete(member1);
		memberRepository.delete(member2);

		long deletedCount = memberRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	public void findByUsernameAndAgeGreaterThan() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
		assertThat(findMembers.get(0).getUsername()).isEqualTo("AAA");
		assertThat(findMembers.get(0).getAge()).isEqualTo(20);
	}

	@Test
	public void testNamedQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> findMembers = memberRepository.findByUsername2("AAA");
		Member member = findMembers.get(0);
		assertThat(member).isEqualTo(m1);
	}

	@Test
	public void testQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> findMembers = memberRepository.findUser("AAA", 10);
		assertThat(findMembers.get(0)).isEqualTo(m1);
	}

	@Test
	public void testUsernameList() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<String> usernameList = memberRepository.findUsernameList();
		for (String s : usernameList) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void findMemberDto() {
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		m1.changeTeam(team);
		memberRepository.save(m1);

		List<MemberDto> memberDtoList = memberRepository.findMemberDto();
		System.out.println("memberDtoList.toString() = " + memberDtoList.toString());
	}

	@Test
	public void findByNames() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> memberList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
		System.out.println("memberList = " + memberList);
	}

	@Test
	public void returnType() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		// 반환타입 컬렉션인 경우 주의점: 조회되는 값이 없어도 Empty Collection으로 반환됨
		List<Member> aaa = memberRepository.findListByUsername("AAA");
		System.out.println("aaa.size() = " + aaa.size());

		Member aaa1 = memberRepository.findMemberByUsername("AAA");
		Optional<Member> aaa2 = memberRepository.findOptionalByUsername("AAA");
	}

	@Test
	public void paging() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		int age = 10;
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

		Page<Member> page = memberRepository.findByAge(age, pageRequest);

		// page dto 전환
		Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

		List<Member> content = page.getContent();

		assertThat(content.size()).isEqualTo(3); // 조회 컨텐츠 수
		assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
		assertThat(page.getNumber()).isEqualTo(0); // 현재 페이지 번호
		assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 수
		assertThat(page.isFirst()).isTrue(); // 첫번째 페이지인지?
		assertThat(page.hasNext()).isTrue(); // 다음 페이지 존재 여부
	}

	@Test
	public void slice() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		int age = 10;
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

		Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

		List<Member> content = page.getContent();

		assertThat(content.size()).isEqualTo(3); // 조회 컨텐츠 수
		assertThat(page.getNumber()).isEqualTo(0); // 현재 페이지 번호
		assertThat(page.isFirst()).isTrue(); // 첫번째 페이지인지?
		assertThat(page.hasNext()).isTrue(); // 다음 페이지 존재 여부
	}

	@Test
	public void bulkUpdate() {
		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));

		// when
		// 벌크연산
		int resultCount = memberRepository.bulkAgePlus(20);

		// 벌크연산은 DB에 다이렉트로 데이터 수정
		// JPA 영속성 컨텍스트(Entity)는 변경 값이 수정되지 않으므로,
		// 1. 영속성 컨텍스트 수동으로 클리어
//		entityManager.flush();
//		entityManager.clear();

		// 2. @Modifying(clearAutomatically = true)

		Member member5 = memberRepository.findMemberByUsername("member5");
		System.out.println("member5.getAge() = " + member5.getAge());

		// then
		assertThat(resultCount).isEqualTo(3);
	}
}