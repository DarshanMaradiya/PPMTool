package io.agileintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {

        if (project.getId() != null) {
            Project existingProject = projectRepository.getProjectById(project.getId());

            if (existingProject != null && !existingProject.getProjectLeader().equals(username)) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if (existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier()
                        + "' can not be updated, because it doesn't exists");
            }
        }

        try {
            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader((user.getUsername()));

            String projectId = project.getProjectIdentifier().toUpperCase();
            project.setProjectIdentifier(projectId);
            if (project.getId() == null) {

                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectId);
            }

            if (project.getId() != null) {
                Backlog backlog = backlogRepository.findByProjectIdentifier(projectId);
                project.setBacklog(backlog);
            }
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException(
                    "Project Id '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectIdentifier, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project Id '" + projectIdentifier.toUpperCase() + "' does not exist");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String Identifier, String username) {
        Project project = findProjectByIdentifier(Identifier, username);

        projectRepository.delete(project);

    }
}
