package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll() {
		return dao.findAll();
	}
	

	/*
	//mockado
	public List<Seller> findAll() {
		List<Seller> list = new ArrayList<>();
		list.add(new Seller(1, "Joao"));
		list.add(new Seller(2, "maria"));
		list.add(new Seller(3, "fulano"));
		return list;
	
	}*/
}
