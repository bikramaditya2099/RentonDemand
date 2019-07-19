package com.rentondemand.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentondemand.beans.ItemBean;
import com.rentondemand.beans.LoginBean;
import com.rentondemand.beans.SearchBean;
import com.rentondemand.beans.UserBean;
import com.rentondemand.dao.RentOnDemandDAO;
import com.rentondemand.exception.RentOnDemandException;
import com.rentondemand.util.InputValidator;
import com.rentondemand.util.RentOnDemandResponse;

@Service
@Transactional
public class RentOnDemandServiceImpl implements RentOnDemandService{
	@Autowired
	private RentOnDemandDAO dao;

	@Override
	public RentOnDemandResponse registerUser(UserBean bean) throws RentOnDemandException {
		InputValidator.validateInputUserDetails(bean);
		return dao.registerUser(bean);
	}

	@Override
	public RentOnDemandResponse loginUser(LoginBean bean) throws RentOnDemandException {
		return dao.loginUser(bean);
	}

	@Override
	public RentOnDemandResponse addItem(ItemBean bean, String sso) throws RentOnDemandException {
		UserBean userBean=dao.getUserBySSO(sso);
		bean.setBean(userBean);
		return dao.addItem(bean, sso);
	}

	@Override
	public RentOnDemandResponse getAllItems(String sso) throws RentOnDemandException {
		return dao.getAllItems(sso);
	}

	@Override
	public RentOnDemandResponse search(SearchBean searchBean, String sso) throws RentOnDemandException {
		return dao.search(searchBean, sso);
		
	}

}
