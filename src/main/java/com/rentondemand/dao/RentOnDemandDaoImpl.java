package com.rentondemand.dao;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;

import com.rentondemand.beans.ItemBean;
import com.rentondemand.beans.LoginBean;
import com.rentondemand.beans.SearchBean;
import com.rentondemand.beans.UserBean;
import com.rentondemand.exception.RentOnDemandException;
import com.rentondemand.exception.RentOnDemandFailedResponse;
import com.rentondemand.util.RentOnDemandResponse;
import com.rentondemand.util.RentOnDemandSuccessResponse;

@Repository
public class RentOnDemandDaoImpl implements RentOnDemandDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public RentOnDemandResponse registerUser(UserBean bean) throws RentOnDemandException {
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(UserBean.class);
		criteria.add(Restrictions.eq("email", bean.getEmail()));
		if(!criteria.list().isEmpty())
			throw new RentOnDemandException(RentOnDemandFailedResponse.USER_ALREADY_EXIST);
		
		session.save(bean);
		return new RentOnDemandResponse(RentOnDemandSuccessResponse.REGISTERED_SUCCESSFULLY);
	}

	@Override
	public RentOnDemandResponse loginUser(LoginBean bean) throws RentOnDemandException {
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(UserBean.class);
		criteria.add(Restrictions.and(Restrictions.eq("email", bean.getEmail()), Restrictions.eq("password", bean.getPassword())));
		if(criteria.list().isEmpty())
			throw new RentOnDemandException(RentOnDemandFailedResponse.LOGIN_FAILED);
		byte[] encodedCredential=Base64.getEncoder().encode((bean.getEmail()+"<>"+bean.getPassword()).getBytes());
		return new RentOnDemandResponse(RentOnDemandSuccessResponse.LOGIN_SUCCESS,new String(encodedCredential));
	}

	@Override
	public boolean validateUser(String encodedString) throws RentOnDemandException {
		if(encodedString.isEmpty()||encodedString==null)
			throw new RentOnDemandException(RentOnDemandFailedResponse.LOGIN_FAILED);
		String[] credentials=new String(Base64.getDecoder().decode(encodedString.getBytes())).split("<>");
		if(credentials.length==0)
			throw new RentOnDemandException(RentOnDemandFailedResponse.LOGIN_FAILED);
		LoginBean bean=new LoginBean();
		bean.setEmail(credentials[0]);
		bean.setPassword(credentials[1]);
		RentOnDemandResponse response=loginUser(bean);
		if(response.getCode()==1001)
		return true;
		else
			return false;
	}

	@Override
	public RentOnDemandResponse addItem(ItemBean bean, String sso) throws RentOnDemandException {
		validateUser(sso);
		Session session=sessionFactory.getCurrentSession();
		session.save(bean);
		return new RentOnDemandResponse(RentOnDemandSuccessResponse.ITEM_ADDED);
	}

	@Override
	public UserBean getUserBySSO(String sso) throws RentOnDemandException {
		if(sso.isEmpty()||sso==null)
			throw new RentOnDemandException(RentOnDemandFailedResponse.LOGIN_FAILED);
		String[] credentials=new String(Base64.getDecoder().decode(sso.getBytes())).split("<>");
		if(credentials.length==0)
			throw new RentOnDemandException(RentOnDemandFailedResponse.LOGIN_FAILED);
		LoginBean bean=new LoginBean();
		bean.setEmail(credentials[0]);
		bean.setPassword(credentials[1]);
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(UserBean.class);
		criteria.add(Restrictions.and(Restrictions.eq("email", bean.getEmail()), Restrictions.eq("password", bean.getPassword())));
		if(criteria.list().isEmpty())
			throw new RentOnDemandException(RentOnDemandFailedResponse.LOGIN_FAILED);
		UserBean userBean=(UserBean) criteria.list().get(0);
		return userBean;
	}

	@Override
	public RentOnDemandResponse getAllItems(String sso) throws RentOnDemandException {
		UserBean bean=getUserBySSO(sso);
		
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(ItemBean.class);
		criteria.createAlias("bean", "bean");
		criteria.add(Restrictions.eq("bean.id",bean.getId() ));
		Set<ItemBean> beans=new HashSet<>();
		beans.addAll(criteria.list());
		return new RentOnDemandResponse(RentOnDemandSuccessResponse.ITEMS_FETCHED,beans);
	}

	@Override
	public RentOnDemandResponse search(SearchBean searchBean, String sso) throws RentOnDemandException {
		validateUser(sso);
		String searchQuery="";
		if(searchBean.getTitle()!=null)
			searchQuery=searchQuery.concat("title='"+searchBean.getTitle()+"'");
		if(searchBean.getDescription()!=null && searchQuery.isEmpty())
			searchQuery=searchQuery.concat("description='"+searchBean.getDescription()+"'");
		else if(searchBean.getDescription()!=null)
			searchQuery=searchQuery.concat(" AND description='"+searchBean.getDescription()+"'");
		
		if(searchBean.getImagetitle()!=null && searchQuery.isEmpty())
			searchQuery=searchQuery.concat("image.title='"+searchBean.getImagetitle()+"'");
		else if(searchBean.getDescription()!=null)
			searchQuery=searchQuery.concat(" AND image.title='"+searchBean.getImagetitle()+"'");

		Session session=sessionFactory.getCurrentSession();
		

		return null;
	}
	
	
	

}
