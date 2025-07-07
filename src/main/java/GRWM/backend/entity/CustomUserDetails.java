package GRWM.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;



@Getter

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;



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
        return this.password;
    }


    /*
    함수명 : getUsername
    기능 : 사용자 식별자를 반환한다; 로그인 아이디
    매개변수
    반환값 : String
     */

    @Override
    public String getUsername() {
        return this.username;
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


    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {

        this.username = username;
        this.password = password;
        this.authorities = authorities;
        // 계정 상태는 기본적으로 true로 설정하거나, 필요시 토큰 클레임에 추가하여 사용
        // 혹은 실제 DB에서 가져온 사용자 정보를 기반으로 설정
    }

    public CustomUserDetails(String username, Collection<? extends GrantedAuthority> authorities) {

        this.username = username;
        this.password = "";
        this.authorities = authorities;
        // 계정 상태는 기본적으로 true로 설정하거나, 필요시 토큰 클레임에 추가하여 사용
        // 혹은 실제 DB에서 가져온 사용자 정보를 기반으로 설정
    }



}
