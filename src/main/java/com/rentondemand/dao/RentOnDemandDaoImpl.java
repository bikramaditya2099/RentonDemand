package com.rentondemand.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rentondemand.beans.BookingBean;
import com.rentondemand.beans.BookingRequest;
import com.rentondemand.beans.ItemBean;
import com.rentondemand.beans.ItemImage;
import com.rentondemand.beans.ItemResponse;
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
		UserBean bean=getUserBySSO(sso);
		String searchQuery="";
		if(searchBean.getTitle()!=null)
			searchQuery=searchQuery.concat("item.title like '"+searchBean.getTitle()+"%'");
		if(searchBean.getDescription()!=null && searchQuery.isEmpty())
			searchQuery=searchQuery.concat("item.description like '"+searchBean.getDescription()+"%'");
		else if(searchBean.getDescription()!=null)
			searchQuery=searchQuery.concat(" AND item.description like '"+searchBean.getDescription()+"%'");
		
		if(searchBean.getImagetitle()!=null && searchQuery.isEmpty())
			searchQuery=searchQuery.concat("image.title like '"+searchBean.getImagetitle()+"%'");
		else if(searchBean.getDescription()!=null)
			searchQuery=searchQuery.concat(" AND image.title like '"+searchBean.getImagetitle()+"%'");

		searchQuery=searchQuery.concat(" AND item.user_id <> "+bean.getId());
		Session session=sessionFactory.getCurrentSession();
		
		String sql="select distinct item.id,item.title,item.description,item.owner,item.rate,GROUP_CONCAT(image.title,'==',image.url ) url from ITEMS item inner join ITEM_IMAGE image on item.id=image.item_id "
				+ "where "+searchQuery
				+ " group by item.id,item.title,item.description,item.owner,item.rate;";
		System.out.println(sql);
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map> list=query.list();
		List<ItemResponse> itemResponses=new ArrayList<ItemResponse>();
		if(!list.isEmpty())
		for(Map map:list) {
			ItemResponse response=new ItemResponse();
			response.setOwner((String) map.get("owner"));
			response.setDescription((String) map.get("description"));
			response.setTitle((String) map.get("title"));
			response.setRate((Double) map.get("rate"));
			response.setItemId((int) map.get("id"));
			String urls=(String) map.get("url");
			String[] images=urls.split(",");
			List<ItemImage> imageUrls=new ArrayList<>();
			if(images.length>1) {
				for(String image:images) {
					String[] img=image.split("==");
					ItemImage itemImage=new ItemImage();
					itemImage.setTitle(img[0]);
					itemImage.setUrl(img[1]);
					imageUrls.add(itemImage);
				}
			}
			else {
				
				String[] img=urls.split("==");
				ItemImage itemImage=new ItemImage();
				itemImage.setTitle(img[0]);
				itemImage.setUrl(img[1]);
				imageUrls.add(itemImage);
			}
			
			response.setImages(imageUrls);
			itemResponses.add(response);
		}
        
		return new RentOnDemandResponse(RentOnDemandSuccessResponse.ITEMS_FETCHED,itemResponses);
	}

	@Override
	public RentOnDemandResponse bookItem(BookingRequest req, String sso) throws RentOnDemandException {
		validateUser(sso);
		Session session=sessionFactory.getCurrentSession();
		Criteria criteria=session.createCriteria(BookingBean.class);
		criteria.createAlias("itemBean", "itemBean");
		criteria.add(Restrictions.eq("itemBean.id", req.getId()));
		List<BookingBean> list=criteria.list();
		if(!list.isEmpty()) {
			for(BookingBean bean:list) {
				
				if(compareDates(req.getFromDate(), req.getToDate(), bean.getFromDate(), bean.getToDate()))
				{
					book(req,session);
				}
				else {
					throw new RentOnDemandException(RentOnDemandFailedResponse.BOOKING_OVERLAP);
				}
			}
		}
		else {
			book(req, session);
		}
		
		return new RentOnDemandResponse(RentOnDemandSuccessResponse.ITEMS_BOOKED);
	}

	private void book(BookingRequest req, Session session) throws RentOnDemandException {
		Criteria criteria1=session.createCriteria(ItemBean.class);
		criteria1.add(Restrictions.eq("id", req.getId()));
		List<ItemBean> beans=criteria1.list();
		ItemBean item=beans.get(0);
		BookingBean bean=new BookingBean();
		bean.setFromDate(req.getFromDate());
		bean.setToDate(req.getToDate());
		Date d1 = null;
		Date d2 = null;
		long diffDays=0;

		try {
			d1 = req.getFromDate();
			d2 = req.getToDate();
			long diff = d2.getTime() - d1.getTime();
			diffDays = diff / (24 * 60 * 60 * 1000);

		} catch (Exception e) {
			throw new RentOnDemandException(RentOnDemandFailedResponse.DATE_PARSE_ERROR);
		}
		
		bean.setTotalPrice(item.getRate()*diffDays);
		session.save(bean);
	}
	
	public static boolean compareDates(Date bookingfromDate,Date bookingtoDate,Date bookedFromdate,Date bookedToDate)
    {
       
        if(bookingfromDate.after(bookedToDate) && bookingtoDate.after(bookedToDate) && bookingfromDate.after(new Date()) ){
            return true;
        }

        else if(bookingfromDate.before(bookedFromdate) && bookingtoDate.before(bookedFromdate) && bookingfromDate.after(new Date())){
            return true;
        }

        else return false;
    }
	

}
