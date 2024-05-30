package com.eden.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eden.dao.ShoesDao;
import com.eden.entity.Shoes;

@Service
@Transactional
public class ShoesServiceImpl implements ShoesService{
	
	@Autowired
	private ShoesDao shoesDao;

	@Override
	public List<Shoes> lists() {
		
		return shoesDao.lists();
	}

	@Override
	public void save(Shoes shoes) {
		
		shoesDao.save(shoes);
	}

	@Override
	public Shoes findById(Integer id) {
		
		return shoesDao.findById(id);
	}

	@Override
	public void update(Shoes shoes) {
		
		shoesDao.update(shoes);
	}

	@Override
	public void delete(Integer id) {
		shoesDao.delete(id);
		
	}

	@Override
	public List<Shoes> searchEmp(String dateBegin, String dateEnd, String topPrice, String lowPrice,
			String searchName) {
		
		return shoesDao.searchEmp(dateBegin,dateEnd,topPrice,lowPrice,searchName);
	}

	

}
