package GRWM.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /*
    함수명 : getPassword
    기능 : 비밀번호를 반환한다.
    매개변수
    반환값 : String
     */

    @Override
    public String getPassword() {
        return member.getPassword();
    }


    /*
    함수명 : getUsername
    기능 : 사용자 식별자를 반환한다; 로그인 아이디
    매개변수
    반환값 : String
     */

    @Override
    public String getUsername() {
        return member.getLoginId();
    }


    /*
    함수명 : isAccountNonExpired
    기능 : 만료된 계정이 아닌지 확인한다. true 반환 시, 계정은 유효하다.
    매개변수
    반환값 : boolean
     */

    @Override
    public boolean isAccountNonExpired() {
        return true; // 실제 로직 구현
    }


    /*
    함수명 : isAccountNonLocked
    기능 : 잠긴 계정이 아닌지 확인한다. true 반환 시, 계정은 사용 가능하다.
    매개변수
    반환값 : boolean
     */

    @Override
    public boolean isAccountNonLocked() {
        return true; // 실제 로직 구현
    }


    /*
    함수명 : isCredentialsNonExpired
    기능 : 계정 비밀번호가 만료되지 않았는지 확인한다. true 반환 시, 비밀번호는 유효하다.
    매개변수
    반환값 : boolean
     */

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 실제 로직 구현
    }


    /*
    함수명 : isEnabled
    기능 : 정지된 계정이 아닌지 확인한다. true 반환 시, 계정은 활성화 되어있다.
    매개변수
    반환값 : boolean
     */

    @Override
    public boolean isEnabled() {
        return true; // 실제 로직 구현
    }
}
