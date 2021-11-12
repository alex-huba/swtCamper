package xtasks.javafx.controller;

import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.contract.TaskStatusDTO;
import xtasks.api.controller.StatisticsController;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class StatisticsViewController {

  @FXML
  public PieChart taskDistributionChart;

  @Autowired
  private MainViewController mainViewController;

  @Autowired
  private StatisticsController statisticsController;

  public void reloadData() {
    try {
      Map<TaskStatusDTO, Double> taskDistribution = statisticsController.taskDistribution();

      ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
        new PieChart.Data(
          TaskStatusDTO.UNASSIGNED.name(),
          taskDistribution.get(TaskStatusDTO.UNASSIGNED)
        ),
        new PieChart.Data(
          TaskStatusDTO.IN_PROGRESS.name(),
          taskDistribution.get(TaskStatusDTO.IN_PROGRESS)
        ),
        new PieChart.Data(
          TaskStatusDTO.COMPLETED.name(),
          taskDistribution.get(TaskStatusDTO.COMPLETED)
        )
      );

      taskDistributionChart.setLabelsVisible(true);

      taskDistributionChart.setData(pieChartData);

      pieChartData.forEach(
        data ->
          data
            .nameProperty()
            .bind(
              Bindings.format(
                "%s (%.0f%%)",
                data.getName(),
                data.pieValueProperty().multiply(100)
              )
            )
      );
    } catch (GenericServiceException e) {
      mainViewController.handleException(e);
    }
  }
}
