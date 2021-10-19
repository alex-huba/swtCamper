package xtasks.api.contract;

public class TaskDTO {

  private Long id;

  private String title;

  private String description;

  private TaskStatusDTO status;

  public TaskDTO() {
    super();
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

  public TaskStatusDTO getStatus() {
    return status;
  }

  public void setStatus(TaskStatusDTO status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return (
      "TaskDTO [id=" +
      id +
      ", title=" +
      title +
      ", description=" +
      description +
      ", status=" +
      status +
      "]"
    );
  }

  public TaskDTO(
    Long id,
    String title,
    String description,
    TaskStatusDTO status
  ) {
    super();
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
  }
}
