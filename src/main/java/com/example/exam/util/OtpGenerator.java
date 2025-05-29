package com.example.exam.util;

import java.util.Random;

public class OtpGenerator {

    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

}
