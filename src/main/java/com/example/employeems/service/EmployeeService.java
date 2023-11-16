package com.example.employeems.service;

import com.example.employeems.dto.EmployeeDTO;
import com.example.employeems.entity.Employee;
import com.example.employeems.repo.EmployeeRepo;
import com.example.employeems.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper modelMapper;

    public String saveEmployee(EmployeeDTO employeeDTO){
        if (employeeRepo.existsById(employeeDTO.getEmpID())){
            return VarList.RSP_DUPLICATED;
        }
        else {
            //This method saves the information from the employeeDTO object into the database
            // by converting it into an Employee entity using a ModelMapper
            // and then saving that entity using the save method of the employeeRepo.
            employeeRepo.save(modelMapper.map(employeeDTO, Employee.class));
            return VarList.RSP_SUCCESS;
        }
    }

    public String updateEmployee(EmployeeDTO employeeDTO){
        if (employeeRepo.existsById(employeeDTO.getEmpID())){
            employeeRepo.save(modelMapper.map(employeeDTO, Employee.class));
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    public List<EmployeeDTO> getAllEmployees(){
        //Employee entities are retrieved from the DB and stored in a list
        List<Employee> employeeList = employeeRepo.findAll();
        //ArrayList<EmployeeDTO> is the target type,
        //indicating that we want to convert the Employee entities to a list of EmployeeDTO objects
        // This is because we need employeeDTO objects to be sent to the frontend as a JSON
        return modelMapper.map(employeeList, new TypeToken<ArrayList<EmployeeDTO>>() {
        }.getType());
    }

    public  EmployeeDTO searchEmployee(int empID){
        if (employeeRepo.existsById(empID)) {
            Employee employee =  employeeRepo.findById(empID).orElse(null);
            return modelMapper.map(employee, EmployeeDTO.class);
        } else {
            return null;
        }
    }

    public String deleteEmployee(int empID){
        if (employeeRepo.existsById(empID)) {
            employeeRepo.deleteById(empID);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_NO_DATA_FOUND;
        }
    }
}
