package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	
	//Dado mockado para exemplo
//	public List<Seller> findAll() {
//		List<Seller> list = new ArrayList<>();
//		list.add(new Seller(1, "Fulano"));;
//		list.add(new Seller(2, "Sicrano"));;
//		return list;
//	
//	}
	
	public List<Seller> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Seller obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
	
	
}
