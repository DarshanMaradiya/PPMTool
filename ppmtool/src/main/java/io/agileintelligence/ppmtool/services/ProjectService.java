package io.agileintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
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

    public Project findProjectByIdentifier(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project Id '" + projectIdentifier.toUpperCase() + "' does not exist");
        }

        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String Identifier) {
        Project project = projectRepository.findByProjectIdentifier(Identifier.toUpperCase());

        if (project == null) {
            throw new ProjectIdException(
                    "Can not delete project with id '" + Identifier.toUpperCase() + "'. Project doesn't exist");
        }

        projectRepository.delete(project);

    }
}
