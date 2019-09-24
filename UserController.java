package com.dev.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dev.beans.Asset;
import com.dev.beans.AssetAllocation;
import com.dev.beans.Employee;
import com.dev.beans.UserMaster;
import com.dev.services.Services;

@RestController
@RequestMapping("/assets")
//@CrossOrigin(origins="localhost:4200", allowCredentials = "true")
public class UserController {
	@Autowired
	Services service;

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserMaster loginService(@RequestBody UserMaster user) {
		UserMaster usermaster = service.loginService(user.getUserid(), user.getUserpassword());
		if (usermaster != null) {
			return usermaster;
		} else {
			return null;
		}
	}

	@PostMapping(path = "/addAsset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Asset addAsset(@RequestBody Asset asset) {
		Asset assets = service.addAsset(asset);
		if (assets != null)
			return assets;
		else
			return null;
	}

	@PutMapping(path = "/updateAsset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Asset updateAsset(@RequestBody Asset asu) {

		Asset updateassets = service.updateAsset(asu.getAssetid(), asu);
		if (updateassets != null) {
			return updateassets;
		} else {
			return null;
		}
	}

	@GetMapping(path = "/getAllAsset", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Asset> getAllDetails() {
		return service.getAllDetails();

	}

	@PostMapping(path = "/addEmployee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee addEmployee(@RequestBody Employee emp) {
		Employee employee = service.addEmployee(emp);
		if (employee != null) {
			return employee;
		} else {
			return null;
		}
	}

	@GetMapping(path = "/getAllAssetAllocation", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AssetAllocation> getAllAssetAllocationService() {
		List<AssetAllocation> ls = service.getAllAssetAllocationService();
		if (ls != null) {
			for (AssetAllocation assetAllocation : ls) {
				System.out.println(ls);
			}
			return ls;

		} else
			return null;

	}

	@PutMapping(path = "/setStatus/{allocationid}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean setStatusService(@PathVariable("allocationid") Integer allocationid,
			@PathVariable("status") String status) {

		if (service.setStatusService(allocationid, status)) {
			return true;
		} else {
			return false;
		}
	}

	@PostMapping(path = "/raiseRequest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AssetAllocation serviceRaiseReq(@RequestBody AssetAllocation asetallocate) {

		AssetAllocation asstallocation = service.serviceRaiseReq(asetallocate);
		if (asstallocation != null)
			return asstallocation;
		else
			return null;
	}

	@DeleteMapping(path = "/deleteAsset/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Asset removeAsset(@PathVariable("id") int r) {
		Asset assets = service.removeAsset(r);
		if (assets != null)
			return assets;
		else
			return null;
	}

	@PostMapping(path = "/viewStatus/{aid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String viewStatusService(@PathVariable("aid") Integer allocationid) {
		return service.viewStatusService(allocationid);
	}
}