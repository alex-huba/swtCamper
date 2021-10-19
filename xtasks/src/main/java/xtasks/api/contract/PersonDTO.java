package xtasks.api.contract;

public class PersonDTO {

  private Long id;

  private String name;

  private int taskLimit;

  public PersonDTO() {
    super();
  }

  public PersonDTO(Long id, String name, int taskLimit) {
    super();
    this.id = id;
    this.name = name;
    this.taskLimit = taskLimit;
  }

  @Override
  public String toString() {
    return (
      "PersonDTO [id=" +
      id +
      ", name=" +
      name +
      ", taskLimit=" +
      taskLimit +
      "]"
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
}
