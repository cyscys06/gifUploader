package com.example.gifUploader.Domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Gif 객체 생성 테스트")
public class GifTest {
    @Test
    void 입력값이_빈_문자열인지_테스트1() {
        String[] input = new String[]{"black", "", "choi", "1234"};
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Gif(input[0], input[1], input[2], input[3])
        ).withMessageContaining("빈 문자열을 입력할 수 없습니다.");
    }

}
