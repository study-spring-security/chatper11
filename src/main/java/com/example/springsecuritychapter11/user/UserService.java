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

    public void auth(User user) {
        Optional<User> o = userRepository.findUserByUsername(user.getUsername());

        if (o.isPresent()) {
            User u = o.get();
            if (passwordEncoder.matches(
                    user.getPassword(),
                    u.getPassword()
            )) {
                renewOpt(u);
            } else {
                throw new BadCredentialsException("Bad credentials.");
            }
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    private void renewOpt(User user) {
        String code = GenerateUtil.generateCode();

        Optional<Otp> userOtp = otpRepository.findOtpByUsername(user.getUsername());

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
        } else {
            Otp otp = new Otp();
            otp.setUsername(user.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
    }

    public boolean check(Otp optToValidate) {
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(optToValidate.getUsername());

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();

            return optToValidate.getCode().equals(otp.getCode());
        }
        return false;
    }
}
