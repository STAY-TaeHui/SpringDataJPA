package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class MemberRepositoryTest
{
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;


    @Test
    public void testMember()
    {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());

        assertThat(findMember.getUsername()).isEqualTo(member.getUsername())
        ;
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
    }

    @Test
    public void paging(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age=10;

        //Page는 0부터 시작
        // 0~3 까지 내림차순으로
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        // 반환타입이 Page라면 totalCount 쿼리까지 같이 날림.

        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        List<Member> content = page.getContent();
        long totalCount = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
    }
    @Test
    public void slice(){
        //Slice는 Total Count를 가져오지 않음
        //Slice는 limit를 +1 더 가져옴.

        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age=10;

        //0~3 까지 내림차순으로
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Slice<Member> slice = memberRepository.findByAge(age, pageRequest);
        // 반환타입이 Page라면 totalCount 쿼리까지 같이 날림.

        List<Member> content = slice.getContent();
//        long totalCount = slice.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
    }

    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("1",10));
        memberRepository.save(new Member("2",10));
        memberRepository.save(new Member("3",20));
        memberRepository.save(new Member("4",21));
        memberRepository.save(new Member("5",40));

        //벌크연산
        int resultCount = memberRepository.bulkAgePlus(20);
        //현재 영속성 컨텍스트 : name : 5    age : 41
        //현재 DB : name : 5    age : 40
        //그렇기 때문에 벌크연산 이후 영속성 컨텍스트를 비워줘야 함.
        em.flush();
        em.clear();

        Member memberByUsername = memberRepository.findMemberByUsername("5");


        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);


        Member m1 = new Member("AAA",10, teamA);
        Member m2 = new Member("BBB",20,teamB);

        memberRepository.save(m1);
        memberRepository.save(m2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        for(Member member:members){
            System.out.println("member = "+member.getUsername());
            System.out.println("member.team = " + member.getTeam());
        }
    }
}