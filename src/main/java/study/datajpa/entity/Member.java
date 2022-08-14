package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter // 실무에서는 entity에 기본적으로 @Setter 잘 사용하지 않음.
public class Member {

	@Id
	@GeneratedValue
	private Long id;
	private String username;

	/*
	JPA 기본적으로 인자없는 생성자 필요
	 */
	protected Member() {
	}

	public Member(String username) {
		this.username = username;
	}
}


