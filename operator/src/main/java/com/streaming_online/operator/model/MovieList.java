/* Movie List entity class
 * Represents a movie in my list view (POJO)
 */

package com.streaming_online.operator.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mylist")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieList {

    public enum MovieState {
        SAVED, PURCHASED, RENTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imdbID", nullable = false)
    private String imdbID;

    @Column(name = "user_id", nullable = false)
    private String userID;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private MovieState state;
}
