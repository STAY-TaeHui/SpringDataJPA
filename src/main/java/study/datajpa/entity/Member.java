package study.datajpa.entity;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","useranme","age","team"})
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

    public Member(String username)
    {
        this.username = username;
    }
}
