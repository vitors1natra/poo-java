package model.dao;

import db.DB;
import model.dao.impl.ProductsDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static ProductsDao createProductsDao() {
		return new ProductsDaoJDBC(DB.getConnection());
	}
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
}
