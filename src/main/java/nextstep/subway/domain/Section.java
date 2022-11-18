package nextstep.subway.domain;

import javax.persistence.*;
import java.util.stream.Stream;

import static nextstep.subway.exception.ErrorMessage.SAME_SUBWAY_SECTION_ERROR;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(name = "fk_section_up_station"))
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(name = "fk_section_down_station"))
    private Station downStation;

    @Embedded
    private Distance distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_section_line"))
    private Line line;

    protected Section() {

    }

    public Section(Station upStation, Station downStation, int distance) {
        validateSection(upStation, downStation);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    private static void validateSection(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(SAME_SUBWAY_SECTION_ERROR.getMessage());
        }
    }

    public void changeLine(Line line) {
        this.line = line;
    }

    public Stream<Station> streamOfStation() {
        return Stream.of(upStation, downStation);
    }

    public void connectUpStationToDownStation(Section section) {
        distance.minus(section.getDistance());
        this.upStation = section.downStation;
    }

    public void connectDownStationToUpStation(Section section) {
        distance.minus(section.getDistance());
        this.downStation = section.upStation;
    }

    public boolean hasUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean hasDownStation(Station station) {
        return downStation.equals(station);
    }

    public void disconnectDownSection(Section downSection) {
        this.downStation = downSection.downStation;
        this.distance.plus(downSection.distance);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }
}
