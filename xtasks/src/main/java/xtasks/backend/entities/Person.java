package xtasks.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  private int taskLimit;

  public Person() {
    super();
  }

  @Override
  public String toString() {
    return (
      "Person{" +
      "id=" +
      id +
      ", name='" +
      name +
      '\'' +
      ", task_limit=" +
      taskLimit +
      '}'
    );
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getTaskLimit() {
    return taskLimit;
  }

  public void setTaskLimit(int taskLimit) {
    this.taskLimit = taskLimit;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + taskLimit;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Person other = (Person) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return taskLimit == other.taskLimit;
  }
}
