package com.rentondemand.util;

import org.springframework.stereotype.Component;

import com.rentondemand.beans.UserBean;
import com.rentondemand.exception.RentOnDemandException;
import com.rentondemand.exception.RentOnDemandFailedResponse;

@Component
public class InputValidator {

	public static void validateInputUserDetails(UserBean bean) throws RentOnDemandException {
		if(bean.getEmail().isEmpty() || bean.getEmail()==null )
			throw new RentOnDemandException(RentOnDemandFailedResponse.EMPTY_EMAIL);
		if(bean.getName().isEmpty() || bean.getName()==null )
			throw new RentOnDemandException(RentOnDemandFailedResponse.EMPTY_NAME);
		if(bean.getPhone().isEmpty() || bean.getPhone()==null )
			throw new RentOnDemandException(RentOnDemandFailedResponse.EMPTY_PHONE);
		if(bean.getPassword().isEmpty() || bean.getPassword()==null )
			throw new RentOnDemandException(RentOnDemandFailedResponse.EMPTY_PASSWORD);
	}
}
