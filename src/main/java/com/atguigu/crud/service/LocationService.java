package com.atguigu.crud.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.atguigu.crud.bean.Location;

@Service
public class LocationService {

	public static List<Location> getSubLocation(Integer id) {
		// TODO Auto-generated method stub
		String sql = "select name from location where pid = " + id;
		
		return null;
	}
	
	 
}
