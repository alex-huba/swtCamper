package xtasks.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Assignment {

  @Id
  @GeneratedValue
  Long id;

  @ManyToOne
  Person person;

  @ManyToOne
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
      prime *
      result +
      ((assignmentStatus == null) ? 0 : assignmentStatus.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((person == null) ? 0 : person.hashCode());
    result = prime * result + ((task == null) ? 0 : task.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Assignment other = (Assignment) obj;
    if (assignmentStatus != other.assignmentStatus) return false;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    if (person == null) {
      if (other.person != null) return false;
    } else if (!person.equals(other.person)) return false;
    if (task == null) {
      return other.task == null;
    } else return task.equals(other.task);
  }

  @Override
  public String toString() {
    return (
      "Assignment [id=" +
      id +
      ", person=" +
      person +
      ", task=" +
      task +
      ", assignmentStatus=" +
      assignmentStatus +
      "]"
    );
  }
}
