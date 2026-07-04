package ec.edu.espe.banquito.core.party.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CORE_PARAMETER")
public class CoreParameter {

    @Id
    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "VALUE_STRING", nullable = false, length = 255)
    private String valueString;

    @Column(name = "DATA_TYPE", nullable = false, length = 15)
    private String dataType;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "LAST_UPDATE", nullable = false)
    private LocalDateTime lastUpdate;

    public CoreParameter() {
    }

    public CoreParameter(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoreParameter that = (CoreParameter) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return "CoreParameter{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", valueString='" + valueString + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}