package com.dp.hloworld.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "category")
public class Category{

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	private long id;
	
	@Column
	private String name;
	
	@Column
	private String image;

}
