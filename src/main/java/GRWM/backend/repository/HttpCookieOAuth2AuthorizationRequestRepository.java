package GRWM.backend.repository;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static GRWM.backend.repository.HttpCookieOAuth2AuthorizationRequestRepository.CookieUtils.getCookie;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

        // 쿠키 이름 정의
        public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
        public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

        // 쿠키 만료 시간 (예: 180초 = 3분)
        private static final int cookieExpireSeconds = 180;

        /**
         * 인증 요청을 쿠키에 로드 (콜백 요청 시 사용)
         */
        @Override
        public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
            return getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                    .map(this::deserialize)
                    .orElse(null);
        }

        /**
         * 인증 요청 및 리디렉션 URI를 쿠키에 저장
         */
        @Override
        public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
            if (authorizationRequest == null) {
                // 요청이 null이면 쿠키를 제거하고 종료
                removeAuthorizationRequestCookies(request, response);
                return;
            }

            // 1. 인증 요청 객체를 쿠키에 직렬화하여 저장
            String requestValue = serialize(authorizationRequest);
            CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, requestValue, cookieExpireSeconds);

            // 2. 콜백 후 최종 리디렉트할 URI를 별도 쿠키에 저장 (필요한 경우)
            String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
            if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
                CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
            }
        }

        /**
         * 인증 요청을 쿠키에서 로드한 후, 쿠키를 제거
         */
        @Override
        public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
            OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
            removeAuthorizationRequestCookies(request, response);
            return authorizationRequest;
        }

        /**
         * 인증 요청과 관련된 모든 쿠키를 제거
         */
        public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        }

        // --- 유틸리티 메서드 (직렬화/역직렬화) ---
        private String serialize(OAuth2AuthorizationRequest authorizationRequest) {
            byte[] bytes = SerializationUtils.serialize(authorizationRequest);
            return Base64.getUrlEncoder().encodeToString(bytes);
        }

        private OAuth2AuthorizationRequest deserialize(Cookie cookie) {
            byte[] bytes = Base64.getUrlDecoder().decode(cookie.getValue());
            return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(bytes);
        }


        public static class CookieUtils {
            // HTTP 요청에서 특정 이름의 쿠키를 찾아 Optional로 반환
            public static java.util.Optional<Cookie> getCookie(HttpServletRequest request, String name) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(name)) {
                            return java.util.Optional.of(cookie);
                        }
                    }
                }
                return java.util.Optional.empty();
            }

            // 새 쿠키를 응답에 추가
            public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
                Cookie cookie = new Cookie(name, value);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(maxAge);
                response.addCookie(cookie);
            }

            // 쿠키를 삭제 (maxAge를 0으로 설정)
            public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
                getCookie(request, name).ifPresent(cookie -> {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                });
            }
        }
}
