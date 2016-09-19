package search4.entities;

import javax.persistence.*;
import java.sql.Date;

/**
 * Auto generated from database table
 */
@Entity
@Table(name = "movies", schema = "search4", catalog = "") //    int tmdbId
	@NamedQuery(name = "MovieEntity.getLastTmdbId", query = "SELECT MAX(moovieEntity.tmdbId) FROM MovieEntity moovieEntity") //TODO limit
public class MovieEntity {
    private int id;
    private int tmdbId;
    private int guideboxId;
    private String title;
    private Date date;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tmdb_id")
    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Basic
    @Column(name = "guidebox_id")
    public int getGuideboxId() {
        return guideboxId;
    }

    public void setGuideboxId(int guideboxId) {
        this.guideboxId = guideboxId;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieEntity that = (MovieEntity) o;

        if (id != that.id) return false;
        if (tmdbId != that.tmdbId) return false;
        if (guideboxId != that.guideboxId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + tmdbId;
        result = 31 * result + guideboxId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MovieEntity{" +
                "id=" + id +
                ", tmdbId=" + tmdbId +
                ", guideboxId=" + guideboxId +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
