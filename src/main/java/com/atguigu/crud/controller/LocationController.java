package com.atguigu.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bean.Location;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.LocationService;

@Controller
public class LocationController {
	@Autowired
	private LocationService locationService;
	
	@RequestMapping(value="/loc/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getSubLocation(@PathVariable("id")Integer id){
		List<Location> location = LocationService.getSubLocation(id);
		
		return Msg.success().add("loc", location);
	}
}
