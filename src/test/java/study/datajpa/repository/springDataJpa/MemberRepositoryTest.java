package study.datajpa.repository.springDataJpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.repository.springDataJpa.MemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

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
}