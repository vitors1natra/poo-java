package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Seller;

public class SellerService {
	
	//mockado
	public List<Seller> findAll() {
		List<Seller> list = new ArrayList<>();
		list.add(new Seller(1, "Joao"));
		list.add(new Seller(2, "maria"));
		list.add(new Seller(3, "fulano"));
		return list;
	}
}
