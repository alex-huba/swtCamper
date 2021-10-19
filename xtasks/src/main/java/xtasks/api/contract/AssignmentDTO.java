package xtasks.api.contract;

import xtasks.backend.entities.AssignmentStatus;
import xtasks.backend.entities.Person;
import xtasks.backend.entities.Task;

public class AssignmentDTO {

  Long id;

  Person person;

  Task task;

  AssignmentStatus assignmentStatus;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  public AssignmentStatus getAssignmentStatus() {
    return assignmentStatus;
  }

  public void setAssignmentStatus(AssignmentStatus assignmentStatus) {
    this.assignmentStatus = assignmentStatus;
  }
}
