package com.rentondemand.service;

import com.rentondemand.beans.ItemBean;
import com.rentondemand.beans.LoginBean;
import com.rentondemand.beans.SearchBean;
import com.rentondemand.beans.UserBean;
import com.rentondemand.exception.RentOnDemandException;
import com.rentondemand.util.RentOnDemandResponse;

public interface RentOnDemandService {

	public RentOnDemandResponse registerUser(UserBean bean) throws RentOnDemandException;
	public RentOnDemandResponse loginUser(LoginBean bean) throws RentOnDemandException;
	public RentOnDemandResponse addItem(ItemBean bean,String sso) throws RentOnDemandException;
	public RentOnDemandResponse getAllItems(String sso) throws RentOnDemandException;
	public RentOnDemandResponse search(SearchBean searchBean,String sso) throws RentOnDemandException;
}
