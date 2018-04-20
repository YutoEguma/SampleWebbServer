package io.github.yutoeguma.dummy;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ダミーのDatabase
 *
 * @author cabos
 */
public class DummyDB {

    @Getter
    public static class Member {

        private static final List<Member> memberList = Arrays.asList(
            new Member("cabos", "password"),
            new Member("sudachi", "passpass"),
            new Member("null", "null")
        );

        private Member(String memberAccount, String password) {
            this.memberAccount = memberAccount;
            this.password = password;
        }

        private String memberAccount;
        private String password;

        public static Optional<Member> searchMember(String memberAccount) {
            return memberList.stream().filter(member -> member.getMemberAccount().equals(memberAccount)).findFirst();
        }
    }
}
