package com.example.springsecuritychapter11.user;

import com.example.springsecuritychapter11.opt.Otp;
import com.example.springsecuritychapter11.opt.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;


    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * 사용자가 인증되면 인증 서버는 사용자를 위한 OTP를 생성한다.
     */
    public void auth(User user) {
        Optional<User> o = userRepository.findUserByUsername(user.getUsername()); // 데이터 베이스에서 사용자 검색

        if (o.isPresent()) { // 사용자가 있으면 암호확인
            User u = o.get();
            if (passwordEncoder.matches(
                    user.getPassword(),
                    u.getPassword()
            )) {
                renewOpt(u); // 암호가 맞으면 새 OPT 생성
            } else {
                throw new BadCredentialsException("Bad credentials."); // 암호가 틀러기나 사용자가 없으면 예외 투척
            }
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    private void renewOpt(User user) {
        String code = GenerateUtil.generateCode(); // OTP를 위한 임의의 수 생성

        Optional<Otp> userOtp = otpRepository.findOtpByUsername(user.getUsername()); // 사용자 이름으로 OTP 검색

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code); // 이 사용자 이름에 대한 OTP가 있으면 값 업데이트
        } else {
            Otp otp = new Otp();
            otp.setUsername(user.getUsername());
            otp.setCode(code);
            otpRepository.save(otp); // 이 사용자 이름에 대한 OTP 가 없으면 생성된 값으로 새 레코드 생성
        }
    }

    /**
     * Otp 검증 메서드
     */
    public boolean check(Otp optToValidate) {
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(optToValidate.getUsername()); // 사용자 이름으로 Opt 검색

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();

            return optToValidate.getCode().equals(otp.getCode()); // 데이터베이스에 OTP가 있고 비즈니스 논리 서버에서 받은 OTP와 일치하면 true 반환
        }
        return false; // 그렇지 않으면 false 반환
    }
}
