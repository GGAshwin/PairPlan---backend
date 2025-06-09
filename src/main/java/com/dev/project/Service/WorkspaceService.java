package com.dev.project.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.project.Entity.UserEntity;
import com.dev.project.Entity.WorkspaceEntity;
import com.dev.project.Repository.WorkspaceRepository;


@Service
public class WorkspaceService {

	public WorkspaceEntity createWorkspace(UserEntity userRequest){
//		var workspaceId = UUID.randomUUID();
		var workspaceName = userRequest.getName() + "'s Workspace";
		String joinCode = generateCode();
		 return WorkspaceEntity.builder().
//				id(workspaceId).
				createdAt(new Date()).
				name(workspaceName).
				joinCode(joinCode).
				build();
	}


		public static String generateCode() {
			Random random = new Random();
			StringBuilder code = new StringBuilder(4);
			for (int i = 0; i < 4; i++) {
				char randomChar = (char) ('A' + random.nextInt(26)); // Generate a random capital letter
				code.append(randomChar);
			}
			return code.toString();
		}
}
