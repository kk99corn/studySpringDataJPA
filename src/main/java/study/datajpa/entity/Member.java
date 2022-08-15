package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter // 실무에서는 entity에 기본적으로 @Setter 잘 사용하지 않음.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
		name = "Member.findByUsername",
		query = "select m from Member m where m.username = :username"
)
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String username;
	private int age;

	// ManyToOne fetch타입 LAZY(지연로딩)으로 변경(default: EAGER(즉시로딩))
	// - fetch타입 즉시로딩으로 사용시 실무에서 성능최적화 힘듦
	@ManyToOne(fetch = FetchType.LAZY)    // N:1 MEMBER:TEAM
	@JoinColumn(name = "team_id")
	private Team team;

	public Member(String username) {
		this.username = username;
	}

	public Member(String username, int age, Team team) {
		this.username = username;
		this.age = age;
		if (team != null) {
			changeTeam(team);
		}
	}

	public Member(String username, int age) {
		this.username = username;
		this.age = age;
	}

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}


