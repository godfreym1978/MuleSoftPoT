package com.jdbcsvc;

import java.util.ArrayList;

import javax.jws.WebService;

@WebService
public interface IAddrCrossRefService {
	ArrayList<EmpResponse> getEmployee(EmpRequest request);
}