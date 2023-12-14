package vttp2023.batch3.ssf.frontcontroller.models;

import java.security.SecureRandom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class Captcha {
    private String question; // What is 3 + 2?
    private Integer answer; // 5

    public Captcha() {
        generateAnswer();
    }

    private int generateNumber(int max) {
        SecureRandom rand = new SecureRandom();
        return rand.nextInt(max) + 1;
    }

    private void generateAnswer() {
        int number1 = generateNumber(50);
        int number2 = generateNumber(50);
        int operator = generateNumber(2);
        switch (operator) {
            case 1 -> {
                answer = number1 + number2;
                question = "What is %d + %d?".formatted(number1, number2);
            }
            case 2 -> {
                answer = number1 - number2;
                question = "What is %d - %d?".formatted(number1, number2);
            }
            case 3 -> {
                answer = number1 / number2;
                question = "What is %d / %d?".formatted(number1, number2);
            }
            case 4 -> {
                answer = number1 * number2;
                question = "What is %d * %d?".formatted(number1, number2);
            }
        }
    }

    public boolean validateCaptcha(int input) {
        return answer == input;

        // if (answer == input) {
        //     return true;
        // } else {
        //     return false;
        // }
    }

}
