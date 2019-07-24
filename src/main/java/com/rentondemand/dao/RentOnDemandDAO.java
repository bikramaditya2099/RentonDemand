package com.rentondemand.dao;

import com.rentondemand.beans.BookingRequest;
import com.rentondemand.beans.ItemBean;
import com.rentondemand.beans.LoginBean;
import com.rentondemand.beans.SearchBean;
import com.rentondemand.beans.UserBean;
import com.rentondemand.exception.RentOnDemandException;
import com.rentondemand.util.RentOnDemandResponse;

public interface RentOnDemandDAO {

	public RentOnDemandResponse registerUser(UserBean bean)  throws RentOnDemandException;
	public RentOnDemandResponse loginUser(LoginBean bean) throws RentOnDemandException;
	public boolean validateUser(String encodedString) throws RentOnDemandException;
	public RentOnDemandResponse addItem(ItemBean bean,String sso) throws RentOnDemandException;
	public UserBean getUserBySSO(String sso)throws RentOnDemandException;
	public RentOnDemandResponse getAllItems(String sso) throws RentOnDemandException;
	public RentOnDemandResponse search(SearchBean searchBean,String sso) throws RentOnDemandException;
	public RentOnDemandResponse bookItem(BookingRequest req,String sso)throws RentOnDemandException;
	
}