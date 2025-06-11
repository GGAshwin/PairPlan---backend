package com.dev.project.Controller;

import java.net.http.HttpResponse;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.project.DTO.JoinDTO;
import com.dev.project.Entity.UserEntity;
import com.dev.project.Entity.WorkspaceEntity;
import com.dev.project.Repository.ChecklistRepository;
import com.dev.project.Repository.UserRepository;
import com.dev.project.Repository.WorkspaceRepository;
import com.dev.project.Service.WorkspaceService;
import org.springframework.security.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/users")
public class UserController {
	private UserRepository userRepository;
	private WorkspaceRepository workspaceRepository;
	private WorkspaceService workspaceService;

	@Autowired
	public UserController(UserRepository userRepository,
			WorkspaceRepository workspaceRepository,
			WorkspaceService workspaceService) {
		this.userRepository = userRepository;
		this.workspaceRepository = workspaceRepository;
		this.workspaceService = workspaceService;

	}

	// Create a new user
	@PostMapping("/")
	public UUID createUser(@RequestBody UserEntity userRequest) {
		// derive workspace details from the user details, save the workspace details then save the user details as
		// it requires the workspace details to be saved first
		String encryptedPassword = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt());
		userRequest.setPassword(encryptedPassword);

		userRepository.save(userRequest);

		System.out.println(userRepository.findAll());
		return userRequest.getId();

	}




	@PostMapping("/join")
	public ResponseEntity<String> joinWorkspace(@RequestBody JoinDTO joinRequest){
		//null check
		if(joinRequest.getJoinCode() == null || joinRequest.getUserId() == null || joinRequest.getJoinCode().isEmpty()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad input, try again!!");
		}

		var validUserOptional = userRepository.findById(joinRequest.getUserId());
			var foundWorkspace = workspaceRepository.findByJoinCode(joinRequest.getJoinCode());
		if(validUserOptional.isPresent()){
			System.out.println(foundWorkspace);
			var workspaceToJoin = foundWorkspace.get();
			var validUser = validUserOptional.get();

			if(validUser.getWorkspace() != null){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already has a workspace!");
			}

			//add users to the workspace
			workspaceToJoin.getUsers().add(validUser);

			validUser.setWorkspace(workspaceToJoin);

			System.out.println(validUser);
			workspaceRepository.save(workspaceToJoin);
			userRepository.save(validUser);

		//after joining the workspace, delete the existing joinCode of the joiner or delete the record itself
		workspaceToJoin.setJoinCode(null);
		return ResponseEntity.status(HttpStatus.OK).body("Joined Workspace Successfully");
		}
		else if (foundWorkspace.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Join Code not found!");

		}
		else{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		}



	}

	//only for testing, remove after using
	@DeleteMapping("/everything")
	public String deleteEverything(){
		workspaceRepository.deleteAll();
		userRepository.deleteAll();

		return "Deleted!!!";
	}
}
