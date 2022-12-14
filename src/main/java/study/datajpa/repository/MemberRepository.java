package study.datajpa.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>
{
    List<Member> findMemberByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String useranme);

    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    //@Modifying 을 꼭 붙여야 한다.  excuteUpdate()
    //clearAutomatically -> em.flush() clear()를 자동으로 해줌.
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
    //벌크연산은 영속성컨텍스트를 거치지 않고 바로 DB에 날려버린다.
    // 때문에 flush clear를 꼭 해줘야 한다.

    @Override
    @EntityGraph(attributePaths = {"team"})   // -> 간단한 fetch join일때만 사용하고 복잡해지면 어처피 JPQL 사용
//    @EntityGraph("Member.All")
    List<Member> findAll();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",value="true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
