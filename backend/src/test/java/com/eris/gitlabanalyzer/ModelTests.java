package com.eris.gitlabanalyzer;

import static org.junit.jupiter.api.Assertions.*;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ModelTests {

    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private GitManagementUserRepository gitManagementUserRepository;

    private Server testServer = new Server("https://csil-git1.cs.surrey.sfu.ca/");
    private User testUser = new User("csl33");
    private UserServer testUserServer = new UserServer(testUser, testServer, "6JAxo_jtzLC2CDMJAAEy");
    private Project testProject = new Project(25514L, "TestProject", "Test Proejct",
            "http:wow!", testServer);
    private GitManagementUser testGitManagementUser = new GitManagementUser("csl33", "Jason Lee", testServer);

    @BeforeAll
    void init(){
        // Many to Many relationship between User -> UserServer <- Sever
        testServer.addUserServer(testUserServer);
        testUser.addUserServer(testUserServer);
        // Add project to server
        testServer.addProject(testProject);
        // Add member to project
        testProject.addGitManagementUser(testGitManagementUser);
        // Connect project to member
        testGitManagementUser.addProject(testProject);
    }

    @Test
    void serverModel() {
        serverRepository.save(testServer);
        Server queryResult = serverRepository.findByServerUrlAndAccessToken("https://csil-git1.cs.surrey.sfu.ca/",
                "6JAxo_jtzLC2CDMJAAEy");
        assertNotNull(queryResult);
        assertEquals(queryResult.getServerUrl(), "https://csil-git1.cs.surrey.sfu.ca/");
    }

    @Test
    void userModel() {
        userRepository.save(testUser);
        User queryResult = userRepository.findUserByUsername("csl33");
        assertNotNull(queryResult);
        assertEquals(queryResult.getUsername(), "csl33");
    }

    @Test
    void userServerModel() {
        Server serverQueryResult = serverRepository.findByServerUrlAndAccessToken("https://csil-git1.cs.surrey.sfu.ca/",
                "6JAxo_jtzLC2CDMJAAEy");
        User userQueryResult = userRepository.findUserByUsername("csl33");
        UserServer userServerOfServer = serverQueryResult.getUserServers().get(0);
        UserServer userServerOfUser = userQueryResult.getUserServers().get(0);
        assertNotNull(userServerOfServer);
        assertNotNull(userServerOfUser);
        assertEquals(userServerOfServer, userServerOfUser);
    }

    @Test
    void projectModel() {
        projectRepository.save(testProject);
        Project queryResult= projectRepository.findById(25514L).orElse(null);
        assertNotNull(queryResult);
        assertEquals(testProject.getId(), queryResult.getId());
        assertEquals(testProject.getName(), queryResult.getName());
        assertEquals(testProject.getNameWithNamespace(), queryResult.getNameWithNamespace());
        assertEquals(testProject.getWebUrl(), queryResult.getWebUrl());
    }
}
