package study.datajpa.repository;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {
    private final EntityManager em;
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class,id);
        return Optional.ofNullable(member);
    }
}