package platform.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import platform.utl.DateFormatter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "codes")
public class Code {

    @Id
    @Column
    @JsonIgnore
    private String codeId;

    @Column
    private String code;

    @Column
    @JsonIgnore
    private LocalDateTime date;

    @JsonProperty("date")
    public String getReadableDate() {
        return DateFormatter.formatToString(date);
    }

    public Code() {
    }

    public Code(String code) {
        this.code = code;
    }


    @JsonCreator
    public Code(
            @JsonProperty("codeId") String codeId,
            @JsonProperty("code") String code,
            @JsonProperty("date") LocalDateTime date)
    {
        this.codeId = codeId;
        this.code = code;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @JsonIgnore
    public String getCodeId() { return codeId;}

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

}