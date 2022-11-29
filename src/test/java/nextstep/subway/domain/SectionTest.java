package nextstep.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.exception.ErrorMessage.SAME_SUBWAY_SECTION_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철구간 테스트")
class SectionTest {

    @DisplayName("같은 구간은 설정할 수 없다.")
    @Test
    void error_same_subway_section() {
        // given
        Station 강남역 = new Station("강남역");

        // when & then
        Assertions.assertThatThrownBy(() -> new Section(강남역, 강남역, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(SAME_SUBWAY_SECTION_ERROR.getMessage());
    }

    @DisplayName("지하철 중간 구간을 추가하면 구간별 길이가 변한다.")
    @Test
    void between_section_insert_change_distance() {
        // given
        Station 신림역 = new Station("신림역");
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");

        // when
        Section 신림_잠실_구간 = new Section(신림역, 잠실역, 10);
        Section 신림_강남_구간 = new Section(신림역, 강남역, 5);
        Line 이호선 = new Line("이호선", "green");
        이호선.addSection(신림_잠실_구간);
        이호선.addSection(신림_강남_구간);

        // then
        assertAll(
                () -> assertThat(이호선.sectionList().size()).isEqualTo(2),
                () -> assertThat(이호선.sectionList()).containsExactly(신림_잠실_구간, 신림_강남_구간),
                () -> assertThat(신림_잠실_구간.getDistance().getDistance()).isEqualTo(5),
                () -> assertThat(신림_강남_구간.getDistance().getDistance()).isEqualTo(5)
        );

    }

    @DisplayName("지하철 중간 구간을 제거하면 다시 구간의 길이는 늘어난다.")
    @Test
    void between_section_delete_change_distance() {
        // given
        Station 신림역 = new Station("신림역");
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");

        // when
        Section 신림_잠실_구간 = new Section(신림역, 잠실역, 10);
        Section 신림_강남_구간 = new Section(신림역, 강남역, 5);
        Line 이호선 = new Line("이호선", "green");
        이호선.addSection(신림_잠실_구간);
        이호선.addSection(신림_강남_구간);

        // then
        assertAll(
                () -> assertThat(이호선.sectionList().size()).isEqualTo(2),
                () -> assertThat(이호선.sectionList()).containsExactly(신림_잠실_구간, 신림_강남_구간),
                () -> assertThat(신림_잠실_구간.getDistance().getDistance()).isEqualTo(5),
                () -> assertThat(신림_강남_구간.getDistance().getDistance()).isEqualTo(5)
        );

        // when
        이호선.deleteSection(강남역);

        // then
        assertThat(이호선.sectionList().get(0).getDistance().getDistance()).isEqualTo(10);

    }

}
