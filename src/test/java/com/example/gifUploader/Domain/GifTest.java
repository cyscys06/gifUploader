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

    @Test
    void 입력값이_빈_문자열인지_테스트2() {
        String[] input = new String[]{"black", "black, human, icecream, cat", "choi", "1234"};
        Gif gif = new Gif(input[0], input[1], input[2], input[3]);
        assertThat(gif.getGifTags().get(0)).isEqualTo("black");
        assertThat(gif.getGifTags().get(1)).isEqualTo("human");
        assertThat(gif.getGifTags().get(2)).isEqualTo("icecream");
        assertThat(gif.getGifTags().get(3)).isEqualTo("cat");
    }
}
