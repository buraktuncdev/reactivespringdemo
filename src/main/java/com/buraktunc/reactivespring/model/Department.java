package com.buraktunc.reactivespring.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("deparment")
public class Department {

	@Id
	private Integer id;
	private String name;
	@Column("user_id")
	private Integer userId;
	private String loc;

}
