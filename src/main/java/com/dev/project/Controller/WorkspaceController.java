package com.dev.project.Controller;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.DTO.WorkspaceCreateDTO;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Entity.WorkspaceEntity;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;
import com.dev.project.Service.WorkspaceService;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
	 private WorkspaceRepository workspaceRepository;
	 private WorkspaceService workspaceService;
	 private UserRepository userRepository;

	 @Autowired
	public WorkspaceController( WorkspaceRepository workspaceRepository,
							   WorkspaceService workspaceService, UserRepository userRepository){
		 this.workspaceRepository = workspaceRepository;
		 this.workspaceService = workspaceService;
		 this.userRepository = userRepository;
	 }

	 @PostMapping("/")
	public ResponseEntity<Object> createWrokspace(@RequestBody WorkspaceCreateDTO workspaceCreateDTO){
		 //create a workspace for userId
		 //find check if userid is valid
		 var userOptional = userRepository.findById(workspaceCreateDTO.getUserId());
		 if(userOptional.isPresent()){
		 var validUser = userOptional.get();
		 //now check if the user already has a associated worksapce
		 if(validUser.getWorkspace() != null){
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already has a workspace!");
		 }
		 WorkspaceEntity workspaceEntity = workspaceService.createWorkspace(validUser);
		 //null check before adding users
		 if (workspaceEntity.getUsers() == null) {
			 workspaceEntity.setUsers(new ArrayList<>());
		 }
		 workspaceEntity.getUsers().add(validUser);
		 workspaceRepository.save(workspaceEntity);
		 //also save the workspace id to user
			 validUser.setWorkspace(workspaceEntity);
			 userRepository.save(validUser);
		 return ResponseEntity.status(HttpStatus.CREATED).body(workspaceEntity);
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!!");

	 }
}
