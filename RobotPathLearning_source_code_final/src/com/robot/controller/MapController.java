package com.robot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.robot.hibernate.Cell;
import com.robot.hibernate.Path;
import com.robot.hibernate.User;

@Controller
@RequestMapping("/map")
public class MapController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView map(HttpSession session) {

		User user = (User) session.getAttribute("USER");
		if (user != null) {
			System.out.println("current user name: " + user.getUsername() + " id: " + user.getId());
			if (user.getPaths().size() > 0) {
				System.out.println(user.getUsername() + "has " + user.getPaths().size() + " paths with "
						+ user.getPaths().get(0).getCells().size() + " cells");
			}
		}else{
			// has not logged in yet
			return new ModelAndView("redirect:/login");
		}

		ModelAndView model = new ModelAndView("views/MapPage");

		if(user.getPaths().size() > 0){
			model.getModel().put("hasPaths", true);
		}
		return model;
	}

	@RequestMapping(value = "savePath", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String post(HttpSession session, @RequestBody Path path) {
		System.out.println("Recieved request to save a path with :" + path.getCells().size() + " cells.");
		for (Cell c : path.getCells()) {
			System.out.println("Cell: ( " + c.getX() + "," + c.getY() + " )");
		}
		User user = (User) session.getAttribute("USER");
		if (user != null) {
			path.setUser(user);
			user.getPaths().add(path);
			path.save();
		}

		return "{\"status\": \"success\"}";
	}
	
	@RequestMapping(value = "loadLatest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Path post(HttpSession session) {
		// find the current user
		Path path = null;
		User user = (User) session.getAttribute("USER");
		if (user != null && user.getPaths().size() > 0) {
			path = user.getPaths().get(user.getPaths().size()-1);
		}
		
		return path;
	
	}

}
