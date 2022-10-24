package study.datajpa.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
public class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    EntityManager em ;


    @Test
    public void testMember() {
        Member memberA = new Member("memberA");
        Member memberB = new Member("memberB");

        Team team1 = new Team("team1");
        Team team2 = new Team("team2");

        Member savedMember = memberJpaRepository.save(memberA);
        Member savedMember2 = memberJpaRepository.save(memberB);


        Member findMember = memberJpaRepository.find(savedMember.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());//OK
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());//OK
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}