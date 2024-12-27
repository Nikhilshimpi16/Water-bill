package com.wnsProject.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wnsProject.Entity.Department;
import com.wnsProject.Repository.DepartmentRepository;
import com.wnsProject.Service.DepartmentService;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
	
	
	@Autowired
	private DepartmentService  departmentService;
	

	@PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return ResponseEntity.ok(departmentService.saveDepartment(department));
    }
	
	@GetMapping
	public ResponseEntity<List<Department>> getAllDepartments()
	{
		return ResponseEntity.ok(departmentService.getAllDepartments());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Department> updateDepartment(@PathVariable Long id , @RequestBody Department updateDepartment)
	{
		try {
			return ResponseEntity.ok(departmentService.updateDepartment(id, updateDepartment));
			
		}catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDepartment(@PathVariable Long  id)
	{
		departmentService.deleteDepartment(id);
		return ResponseEntity.noContent().build();
	}

}
