package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProductsDao;
import model.entities.Products;

public class ProductsService {
	private ProductsDao dao = DaoFactory.createProductsDao();

	//Dado mockado para exemplo
//	public List<Products> findAll() {
//		List<Products> list = new ArrayList<>();
//
//		Seller seller1 = new Seller(1, "Fulano");
//		Seller seller2 = new Seller(2, "Sicrano");
//
//		list.add(new Products(1, "xxxxxx", "Cd's", new Date(22/11/1999), 20.00, seller1));
//		list.add(new Products(2, "yyyyyy", "Vinil", new Date(13/01/1980), 50.00, seller2));
//		list.add(new Products(3, "mmmmmm", "Revista", new Date(20/8/2005), 15.00, seller1));
//		list.add(new Products(4, "nnnnnn", "HQ", new Date(30/05/2002), 70.00, seller2));
//		return list;
//	}
	
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
