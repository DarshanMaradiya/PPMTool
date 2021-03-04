package io.agileintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectTaskNotFoundException;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        // Exceptions: Project not found
        Project project = projectService.findProjectByIdentifier(projectIdentifier, username);

        // All ProjectTasks belongs to a backlog of a specific project for sure
        // Backlog backlog =
        // backlogRepository.findByProjectIdentifier(projectIdentifier);
        Backlog backlog = project.getBacklog();

        if (projectTask.getId() != null) {
            // throw new WrongRequestMethodException("Wrong Request Method is used to update
            // the project task");

        }
        // Set the backlog to a projectTask
        projectTask.setBacklog(backlog);
        // We want our project sequence to be like this: IDPRO-1, IDPRO-2, ..., 100 101
        Integer BacklogSequence = backlog.getPTSequence();
        // If we delete IDPRO-2 then next assignment should be next id in sequence,
        // but not IDPRO-2 for sure
        // Update the backlog SEQUENCE
        backlog.setPTSequence(++BacklogSequence);

        // Add Sequence to Project Task
        projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        // INTIAL priority when priority is null
        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            // in the future we need to remove the comment
            projectTask.setPriority(3); // Low priority
        }
        // INTIAL status when status is null
        if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
            projectTask.setStatus("TO_DO");
        }
        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String backlog_id, String username) {
        Project project = projectService.findProjectByIdentifier(backlog_id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String projectIdentifier, String projectSequence,
            String username) {
        // Make sure we are searching on the backlog exists
        Project project = projectService.findProjectByIdentifier(projectIdentifier, username);

        Backlog backlog = project.getBacklog();

        // make sure task exists
        // make sure the backlog/projectId in the path correspond to the right project
        ProjectTask projectTask = projectTaskRepository.findByProjectSequenceAndBacklog(projectSequence, backlog);

        if (projectTask == null) {
            throw new ProjectTaskNotFoundException(
                    "Project task " + projectSequence + " does not exist in project " + projectIdentifier);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequenceAndBacklogId(ProjectTask updatedTask, String backlog_id,
            String projectSequence, String username) {
        if (updatedTask.getId() == null) {
            throw new ProjectTaskNotFoundException("Project task Id can't be null");
        }
        // Find existing project task
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, projectSequence, username);
        if (!projectTask.getId().equals(updatedTask.getId())) {
            throw new ProjectTaskNotFoundException("Invalid Project task Id");
        }
        // Replace it with updated task
        // save update
        return projectTaskRepository.save(updatedTask);
    }

    public void deleteProjectTaskByProjectSequenceAndBacklogId(String backlog_id, String projectSequence,
            String username) {
        // Find existing project task
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, projectSequence, username);
        // delete the task
        System.out.println("Deleting");
        projectTaskRepository.delete(projectTask);
    }
}
