package com.carrental.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.carrental.daos.BookingRepository;
import com.carrental.daos.FeedbackRepository;
import com.carrental.daos.PaymentRepository;
import com.carrental.entities.Car;
import com.carrental.entities.Booking;
import com.carrental.entities.Customer;
import com.carrental.entities.Feedback;
import com.carrental.entities.Payment;
import com.carrental.entities.Variant;
import com.carrental.models.BookingCompleteDTO;
import com.carrental.models.BookingDTO;
import com.carrental.models.BookingStatusDTO;

@Service
public class BookingService {

	@Autowired private BookingRepository brepo;
	@Autowired private PaymentRepository prepo;
	@Autowired private CustomerService csrv;
	@Autowired private VariantService vsrv;
	@Autowired private CarService bksrv;
	@Autowired private FeedbackRepository frepo;
	
	public void saveBooking(BookingDTO dto) {
		System.out.println(dto);
		Customer customer=csrv.findByUserId(dto.getUserid());
		Variant variant=vsrv.findById(dto.getVarid());
		
		Booking bk=new Booking();
		BeanUtils.copyProperties(dto, bk);
		bk.setCustomer(customer);
		bk.setVariant(variant);
		System.out.println(bk);
		brepo.save(bk);
		
		Payment pmt=new Payment();
		BeanUtils.copyProperties(dto, pmt);
		pmt.setRemarks("Booking Amount");
		pmt.setBooking(bk);
		pmt.setAmount(dto.getAdvance());
		System.out.println(pmt);
		prepo.save(pmt);
		
	}
	
	public void updateBooking(BookingStatusDTO dto) {
		Booking bk=findById(dto.getBid());
		Car car=crsrv.findById(dto.getBno());
		bk.setCar(car);
		bk.setStatus("Confirmed");
		brepo.save(bk);
		
		car.setStatus("Not Available");
		crsrv.updateBike(car);
	}
	
	public void completeBooking(BookingCompleteDTO dto) {
		Booking bk=findById(dto.getBid());
		
		Car cr=bk.getCar();
		cr.setStatus("Available");
		crsrv.updateBike(cr);		
		
		Payment pmt=new Payment();
		pmt.setAmount(dto.getAmount());
		pmt.setBooking(bk);
		pmt.setNameoncard(dto.getNameoncard());
		pmt.setCardno(dto.getCardno());
		pmt.setRemarks("Payment completed");
		pmt.setIscompleted(true);
		prepo.save(pmt);
		
		Feedback fb=new Feedback();
		fb.setCustomer(bk.getCustomer());
		fb.setDescr(dto.getFeedback());
		fb.setRatings(dto.getRatings());
		
		frepo.save(fb);
	}
	
	public List<Feedback> allFeedbacks(){
		return frepo.findAll(Sort.by(Direction.DESC, "id"));
	}
	
	public void cancelBooking(int id) {
		List<Payment> pmts=prepo.findByBooking(brepo.getById(id));
		prepo.deleteAll(pmts);
		brepo.delete(brepo.getById(id));
	}
	
	public Booking findById(int id) {
		return brepo.getById(id);
	}
	
	public List<Booking> findAllBookings(){
		return brepo.findAll(Sort.by(Direction.DESC, "id"));
	}
	
	public List<Payment> findAllPayments(){
		return prepo.findAll(Sort.by(Direction.DESC, "id"));
	}
	
	public List<Booking> findUserBookings(String userid){
		return brepo.findByCustomer(csrv.findByUserId(userid));
	}
	
	public List<Payment> findBookingPayments(int id){
		return prepo.findByBooking(brepo.getById(id));
	}
}
