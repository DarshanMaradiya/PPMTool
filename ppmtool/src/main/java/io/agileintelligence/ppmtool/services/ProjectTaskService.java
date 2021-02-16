package io.agileintelligence.ppmtool.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.exceptions.ProjectTaskNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        // Exceptions: Project not found
        try {
            // All ProjectTasks belongs to a backlog of a specific project for sure
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
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
            if (/* projectTask.getPriority() == 0 || */projectTask.getPriority() == null) {
                // in the future we need to remove the comment
                projectTask.setPriority(3); // Low priority
            }
            // INTIAL status when status is null
            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        } catch (Exception error) {
            throw new ProjectNotFoundException("Project not found");
        }

    }

    public Iterable<ProjectTask> findBacklogById(String backlog_id) {
        Project project = projectRepository.findByProjectIdentifier(backlog_id);
        if (project == null) {
            throw new ProjectNotFoundException("Project with ID " + backlog_id + " does not exist");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findProjectTaskByProjectSequence(String backlog_id, String projectSequence) {
        // Make sure we are searching on the backlog exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null) {
            throw new ProjectNotFoundException("Project with ID " + backlog_id + " does not exist");
        }
        // make sure task exists
        // make sure the backlog/projectId in the path correspond to the right project
        ProjectTask projectTask = projectTaskRepository.findByProjectSequenceAndBacklog(projectSequence, backlog);

        if (projectTask == null) {
            throw new ProjectTaskNotFoundException(
                    "Project task " + projectSequence + " does not exist in project " + backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequenceAndBacklogId(ProjectTask updatedTask, String backlog_id,
            String projectSequence) {
        // Find existing project task
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, projectSequence);
        // Replace it with updated task
        // save update
        return projectTaskRepository.save(updatedTask);
    }

    public void deleteProjectTaskByProjectSequenceAndBacklogId(String backlog_id, String projectSequence) {
        // Find existing project task
        ProjectTask projectTask = findProjectTaskByProjectSequence(backlog_id, projectSequence);
        // delete the task
        System.out.println("Deleting");
        projectTaskRepository.delete(projectTask);
    }
}
