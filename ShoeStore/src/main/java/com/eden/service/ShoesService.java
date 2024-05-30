package com.eden.service;

import java.util.List;

import com.eden.entity.Shoes;

public interface ShoesService {

	List<Shoes> lists();

	void save(Shoes shoes);

	Shoes findById(Integer id);

	void update(Shoes shoes);

	void delete(Integer id);

	List<Shoes> searchEmp(String dateBegin, String dateEnd, String topPrice, String lowPrice, String searchName);

	

}
