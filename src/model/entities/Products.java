package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Products implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String category;
	private Date releaseDate;
	private Double price;
	
	private Seller seller;
	
	public Products() {
	}

	public Products(Integer id, String name, String category, Date releaseDate, Double price, Seller seller) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.releaseDate = releaseDate;
		this.price = price;
		this.seller = seller;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Products other = (Products) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Products [id=" + id + ", name=" + name + ", category=" + category + ", releaseDate=" + releaseDate + ", price="
				+ price + ", seller=" + seller + "]";
	}
}
