package com.eden.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.eden.anno.Log;
import com.eden.entity.Shoes;
import com.eden.service.ShoesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("shoes")
public class ShoesController {
	
	@Autowired
	private ShoesService shoesService;
	
	@Value("${file.upload.dir}")
	private String realpath;
	
	@RequestMapping("search")
	public String searchEmp(String dateBegin,String dateEnd,String topPrice,
			String lowPrice, String searchName,Model model) {
		
		dateBegin=dateBegin.trim();
		dateEnd=dateEnd.trim();
		topPrice=topPrice.trim();
		lowPrice=lowPrice.trim();
		searchName=searchName.trim();
		
		log.info("開始サイズ：{},完了サイズ：{},最高値段：{},最低値段：{},名前：{}",dateBegin,dateEnd,topPrice,lowPrice,searchName);
		
		List<Shoes> shoes=shoesService.searchEmp(dateBegin,dateEnd,topPrice,lowPrice,searchName);
		
		model.addAttribute("shoesList", shoes);
		
		return "emplist";
	}
	
	@Log
	@RequestMapping("delete")
	public String delete(Integer id) {
		log.info("削除の商品ID：{}",id);
		
		String imageUrl = shoesService.findById(id).getImageUrl();
		
		File file=new File(realpath,imageUrl);
		if(file.exists()) file.delete();
		
		shoesService.delete(id);
		
		return "redirect:/shoes/lists";
	}
	
	@Log
	@RequestMapping("update")
	public String update(Shoes shoes,MultipartFile img) throws IllegalStateException, IOException {
		log.info("更新した商品ID：{},名前：{},値段：{},サイズ：{}",
				shoes.getId(),shoes.getName(),shoes.getPrice(),shoes.getSize());
		boolean notempty=!img.isEmpty();
		log.info("写真更新するかどうか：{}",notempty);
		
		if(notempty) {
			String oldimageUrl=shoesService.findById(shoes.getId()).getImageUrl();
			
			File file= new File(realpath,oldimageUrl);
			
			if(file.exists()) {
				file.delete();
			}
			
			String originalFilename = img.getOriginalFilename();
			
			//抽出するメソッド
			String newFileName = uploadImageUrl(img,originalFilename);			
			
			shoes.setImageUrl(newFileName);
		}
		
		/*if(employee.getPhoto().isEmpty()) {
			employee.setPhoto(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		}*/
		
		shoesService.update(shoes);
				
		return "redirect:/shoes/lists";
	}	
	
	

	@RequestMapping("detail")
	public String detail(Integer id,Model model) {
		log.info("更新ID：{}",id);
		
		Shoes shoes= shoesService.findById(id);
		
		model.addAttribute("shoes",shoes);
		return "updateEmp";
	}
	
	@Log
	@RequestMapping("save")
	public String save(Shoes shoes,MultipartFile img) throws IllegalStateException, IOException {
		log.debug("名前：{},値段：{},サイズ：{}",shoes.getName(),shoes.getPrice(),shoes.getSize());
		String originalFilename = img.getOriginalFilename();
		log.debug("ファイルの名：{},ファイルのサイズ：{},アップロードアドレス：{}",originalFilename,img.getSize(),realpath);
		
		String newFileName = uploadImageUrl(img,originalFilename);	
		
		shoes.setImageUrl(newFileName);
		
		shoesService.save(shoes);
		
		return "redirect:/shoes/lists";
	}


	@GetMapping("lists")
	public String lists(Model model) {
		log.debug("全部商品を照会する");
		
		List<Shoes> shoesList= shoesService.lists();
		
		model.addAttribute("shoesList", shoesList);
		return "emplist";
	}
	
	private String uploadImageUrl(MultipartFile img,String originalFilename) throws IllegalStateException, IOException {		
		
		
		String fileNamePrefix = originalFilename.substring(0,originalFilename.lastIndexOf("."));
		
		String fileTime=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		
		String fileNamesuffix =originalFilename.substring(originalFilename.lastIndexOf("."));//.gif
		
		String newFileName=fileNamePrefix+fileTime+fileNamesuffix;
		
		img.transferTo(new File(realpath,newFileName));
		
		return newFileName;
	}
}
