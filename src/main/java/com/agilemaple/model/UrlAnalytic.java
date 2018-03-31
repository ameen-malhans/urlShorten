package com.agilemaple.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="url_analytic")
public class UrlAnalytic implements Serializable{
	

	private static final long serialVersionUID = -1042700675059000637L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="count")
	private Integer count;
	
	@OneToOne
	@JoinColumn(name = "storeurl_id")
	private UrlShortner urlShortner;
	
	public UrlShortner getUrlShortner() {
		return urlShortner;
	}

	public void setUrlShortner(UrlShortner urlShortner) {
		this.urlShortner = urlShortner;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
