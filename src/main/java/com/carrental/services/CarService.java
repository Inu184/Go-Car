package com.carrental.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.carrental.daos.CarRepository;
import com.carrental.entities.Car;
import com.carrental.models.CarDTO;

@Service
public class CarService {

	@Autowired private CarRepository crepo;
	@Autowired private VariantService vsrv;
	
	public void saveCar(CarDTO dto) {
		Car car=new Car();
		if(crepo.existsById(dto.getId())) {			
			car=crepo.getById(dto.getId());
		}		
		BeanUtils.copyProperties(dto, car);
		car.setVariant(vsrv.findById(dto.getVarid()));
		crepo.save(car);
	}
	
	public void updateBike(Car cr) {
		crepo.save(cr);
	}
	
	public List<Car> listAll(){
		return crepo.findAll(Sort.by(Direction.DESC, "createdon"));
	}
	
	public Car findById(String id) {
		return crepo.getById(id);
	}
	
	public List<Car> filterCars(int id){
		System.out.println("Filter id "+id);
		if(id==1)
			return crepo.findByStatus("Available");
		else
			return crepo.findByStatus("Not Available");
	}
	
	public List<Car> listVariantCars(int varid){
		return crepo.findByVariantAndStatus(vsrv.findById(varid),"Available");
	}
	
	public void deleteCar(String id) {
		if(crepo.existsById(id)) {
			crepo.delete(crepo.getById(id));
		}
	}	
}
