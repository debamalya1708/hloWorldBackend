package com.dp.hloworld.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.SpringSecurityCoreVersion;
import java.util.List;

@Data
@Builder
public class UserResponse {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private long id;

    private String email;

    private String contact;

    private String name;

    private List<Long> likesList;

    private List<Long> viewsList;

    private List<Long> favouritesList;
}
