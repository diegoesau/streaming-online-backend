/* Movie entity class
 * Represents a movie in the system (POJO)
 */

package com.streaming_online.search.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "movies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name = "imdbID", unique = true, nullable = false, length = 20)
    private String imdbID;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "Year", nullable = false)
    private Integer year;

    @Column(name = "Runtime", length = 50)
    private String runtime;

    @Column(name = "Genre", length = 255)
    private String genre;

    @Column(name = "Director", nullable = false, length = 255)
    private String director;

    @Column(name = "Actors", columnDefinition = "TEXT")
    private String actors;

    @Column(name = "Plot", nullable = false, columnDefinition = "TEXT")
    private String plot;

    @Column(name = "Poster", nullable = false, length = 500)
    private String poster;

    @Column(name = "imdbRating", precision = 3)
    private Float imdbRating;
}
