package io.agileintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        // Exceptions: Project not found

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
    }
}
