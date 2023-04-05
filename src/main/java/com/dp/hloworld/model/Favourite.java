package com.dp.hloworld.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "favourites")
public class Favourite {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "video_id")
    private Long videoId;

    @Column(name = "user_id")
    private Long userId;

}
