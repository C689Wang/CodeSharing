package platform.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import platform.utl.DateFormatter;

import java.time.LocalDateTime;

public class Code {

    private String code;
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
            @JsonProperty("code") String code,
            @JsonProperty("date") LocalDateTime date)
    {
        this.code = code;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}