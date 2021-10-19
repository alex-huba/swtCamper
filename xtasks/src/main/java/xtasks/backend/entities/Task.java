package xtasks.backend.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task {

  @Id
  @GeneratedValue
  private Long id;

  private String title;
  private String description;

  public Task(String title) {
    this.title = title;
  }

  public Task(String title, String description) {
    super();
    this.title = title;
    this.description = description;
  }

  public Task() {
    super();
  }

  @Override
  public String toString() {
    return (
      "Task [id=" +
      id +
      ", title=" +
      title +
      ", description=" +
      description +
      "]"
    );
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
      prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Task other = (Task) obj;
    if (description == null) {
      if (other.description != null) return false;
    } else if (!description.equals(other.description)) return false;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    if (title == null) {
      return other.title == null;
    } else return title.equals(other.title);
  }
}
