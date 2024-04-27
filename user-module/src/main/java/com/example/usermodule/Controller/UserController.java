package com.example.usermodule.Controller;

import com.example.usermodule.Dto.ChangePasswordDto;
import com.example.usermodule.Dto.MailAuthDto;
import com.example.usermodule.Dto.SignUpDto;
import com.example.usermodule.Dto.UserUpdateDto;
import com.example.usermodule.Security.JWT.JwtProvider;
import com.example.usermodule.Security.JWT.JwtToken;
import com.example.usermodule.Security.JWT.RefreshTokenRequest;
import com.example.usermodule.Service.EmailAuthService;
import com.example.usermodule.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final JwtProvider jwtProvider;

    /**
     * 회원가입
     */

    // 회원가입 1차
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestPart("signUpDto") SignUpDto signUpDto,
                                         @RequestPart(value = "profileImage") MultipartFile profileImage) throws JsonProcessingException {
        userService.preSignUp(signUpDto, profileImage); // 회원가입 유효성 검사 및 Redis에 저장
        emailAuthService.sendMail(signUpDto.getEmail()); // 이메일 전송
        return ResponseEntity.ok("회원가입 요청 완료.");
    }

    // 회원가입 2차
    @PostMapping("/signup/confirm")
    public ResponseEntity<String> signUpConfirm(@RequestBody MailAuthDto mailAuthDto) {
        boolean result = userService.signUpConfirm(mailAuthDto.getNumber());
        return ResponseEntity.ok(result ? "인증 성공" : "인증 실패");
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public JwtToken login(@RequestBody SignUpDto signUpDto) {
        return userService.login(signUpDto.getEmail(), signUpDto.getPassword());
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test");
    }

    /**
     * 로그아웃
     */
    //모든 기기에서 로그아웃 처리
    @PostMapping("/logoutall")
    public ResponseEntity<String> logoutForAll() {
        userService.logoutForAll();
        return ResponseEntity.ok("로그아웃 완료.");
    }

    //현재 기기에서 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<String> logoutNow(HttpServletRequest request) {
        String token = resolveToken(request);
        userService.logout(token);
        return ResponseEntity.ok("로그아웃 완료.");
    }

    /**
     * 회원정보 수정
     */
    //회원 수정 폼 생성
    @GetMapping("/info")
    public SignUpDto getUserInfo(@RequestBody String password) {
        return userService.getUserInfo(password);
    }

    //회원정보 수정
    @PostMapping("/update")
    public ResponseEntity<String> updateUserInfo(@RequestPart UserUpdateDto updateDto,
                                                 @RequestPart(value = "profileImage") MultipartFile profileImage) {
        userService.updateUser(updateDto, profileImage);
        return ResponseEntity.ok("회원정보 수정 완료.");
    }

    //비밀번호 변경
    @PostMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto.getPassword(), changePasswordDto.getNewPassword());
        return ResponseEntity.ok("비밀번호 변경 완료.");
    }

    // user 접근 권한 확인 (삭제 !!!!)
    @PostMapping("/now")
    public String user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            log.info("현재 사용자: {}", ((UserDetails) authentication.getPrincipal()).getUsername());
            log.info("현재 사용자 role: {}", ((UserDetails) authentication.getPrincipal()).getAuthorities());

            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            throw new IllegalStateException("인증된 사용자를 찾을 수 없습니다.");
        }
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/now/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String newAccessToken = jwtProvider.refreshAccessToken(refreshTokenRequest.getRefreshToken()).getAccessToken();
        return ResponseEntity.ok(newAccessToken);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
