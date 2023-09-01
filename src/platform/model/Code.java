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
    private String id;

    @Column
    private String code;

    @Column
    @JsonIgnore
    private int views;

    @Column
    @JsonIgnore
    private int time;


    @Column
    @JsonIgnore
    private LocalDateTime date;

    @JsonProperty("date")
    public String getReadableDate() {
        return DateFormatter.formatToString(date);
    }

    // Flag to distinguish if the number of views is restricted
    @Column
    @JsonIgnore
    private boolean viewsAreRestricted;

    // Flag to distinguish if the view time is restricted
    @Column
    @JsonIgnore
    private boolean viewTimeIsRestricted;

    public Code() {
    }

    public Code(String code) {
        this.code = code;
    }


    @JsonCreator
    public Code(
            @JsonProperty("id") String id,
            @JsonProperty("code") String code,
            @JsonProperty("date") LocalDateTime date,
            @JsonProperty("views") int views,
            @JsonProperty("time") int time,
            @JsonProperty("viewsAreRestricted") boolean viewsAreRestricted,
            @JsonProperty("viewTimeIsRestricted") boolean viewTimeIsRestricted)

    {
        this.id = id;
        this.code = code;
        this.date = date;
        this.views = views;
        this.time = time;
        this.viewsAreRestricted = viewsAreRestricted;
        this.viewTimeIsRestricted = viewTimeIsRestricted;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @JsonIgnore
    public String getId() { return id;}

    public int getViews() { return views;}

    public int getTime() { return time;}

    @JsonIgnore
    public boolean getViewsAreRestricted() { return viewsAreRestricted;}

    @JsonIgnore
    public boolean getViewTimeIsRestricted() { return viewTimeIsRestricted;}

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setViews(int views) {
        this.views = views;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public void setViewTimeIsRestricted(boolean viewTimeIsRestricted) {
        this.viewTimeIsRestricted = viewTimeIsRestricted;
    }
    public void setViewsAreRestricted(boolean viewsAreRestricted) {
        this.viewsAreRestricted = viewsAreRestricted;
    }

}