package com.rentondemand.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentondemand.beans.ItemBean;
import com.rentondemand.beans.LoginBean;
import com.rentondemand.beans.SearchBean;
import com.rentondemand.beans.UserBean;
import com.rentondemand.exception.RentOnDemandException;
import com.rentondemand.service.RentOnDemandService;
import com.rentondemand.util.RentOnDemandResponse;

@RestController
@RequestMapping(value={"/rentondemand"})
public class RentOnDemandController {

	@Autowired
	RentOnDemandService demandService;
	private static final Logger LOGGER = LogManager.getLogger(RentOnDemandController.class);
	
	@PostMapping(value="/registerUser",headers="Accept=application/json")
	public ResponseEntity<RentOnDemandResponse> registerUser(@RequestBody UserBean bean){
		LOGGER.info("registering user");
		try {
			RentOnDemandResponse response=demandService.registerUser(bean);
			return new ResponseEntity<RentOnDemandResponse>(response,HttpStatus.CREATED);
		} catch (RentOnDemandException e) {
			return new ResponseEntity<RentOnDemandResponse>(new RentOnDemandResponse(e),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping(value="/loginUser",headers="Accept=application/json")
	public ResponseEntity<RentOnDemandResponse> loginUser(@RequestBody LoginBean bean){
		LOGGER.info("Logging in User");
		try {
			RentOnDemandResponse response=demandService.loginUser(bean);
			return new ResponseEntity<RentOnDemandResponse>(response,HttpStatus.OK);
		} catch (RentOnDemandException e) {
			return new ResponseEntity<RentOnDemandResponse>(new RentOnDemandResponse(e),HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value="/addItem",headers="Accept=application/json")
	public ResponseEntity<RentOnDemandResponse> addItem(@RequestBody ItemBean bean, @RequestHeader("sso") String sso){
		LOGGER.info("Adding Item");
		try {
			RentOnDemandResponse response=demandService.addItem(bean, sso);
			return new ResponseEntity<RentOnDemandResponse>(response,HttpStatus.OK);
		} catch (RentOnDemandException e) {
			return new ResponseEntity<RentOnDemandResponse>(new RentOnDemandResponse(e),HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping(value = "/getAllItems",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RentOnDemandResponse> getAllItems(@RequestHeader("sso") String sso){
		try {
			RentOnDemandResponse response=demandService.getAllItems(sso);
			return new ResponseEntity<RentOnDemandResponse>(response,HttpStatus.OK);
		} catch (RentOnDemandException e) {
			return new ResponseEntity<RentOnDemandResponse>(new RentOnDemandResponse(e),HttpStatus.NOT_FOUND);
		}
	}
	@PostMapping(value="/search",headers="Accept=application/json")
	public ResponseEntity<RentOnDemandResponse> getSearchItems(@RequestBody SearchBean bean,@RequestHeader("sso") String sso){
		RentOnDemandResponse response;
		try {
			response = demandService.search(bean, sso);
			return new ResponseEntity<RentOnDemandResponse>(response,HttpStatus.OK);
		} catch (RentOnDemandException e) {
			return new ResponseEntity<RentOnDemandResponse>(new RentOnDemandResponse(e),HttpStatus.NOT_FOUND);
		}
		
	}
}
