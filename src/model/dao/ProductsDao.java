package model.dao;

import java.util.List;

import model.entities.Seller;
import model.entities.Products;

public interface ProductsDao {

	void insert(Products obj);
	void update(Products obj);
	void deleteById(Integer id);
	Products findById(Integer id);
	List<Products> findAll();
	List<Products> findBySeller(Seller seller);
}
