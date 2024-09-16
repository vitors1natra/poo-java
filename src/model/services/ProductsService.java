package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProductsDao;
import model.entities.Products;

public class ProductsService {
	
	private ProductsDao dao = DaoFactory.createProductsDao();
	
	/*
	//mockado
	public List<Products> findAll() {
		List<Products> list = new ArrayList<>();
		list.add(new Products(1, "Joao"));
		list.add(new Products(2, "maria"));
		list.add(new Products(3, "fulano"));
		return list;
	
	}*/
	
	
	public List<Products> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Products obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Products obj) {
		dao.deleteById(obj.getId()); 
	}
	
	
}
