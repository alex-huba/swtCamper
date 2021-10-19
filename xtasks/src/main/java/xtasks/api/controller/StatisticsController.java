package xtasks.api.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xtasks.api.ModelMapper;
import xtasks.api.contract.IStatisticsController;
import xtasks.api.contract.TaskStatusDTO;
import xtasks.backend.entities.TaskStatus;
import xtasks.backend.services.StatisticsService;
import xtasks.backend.services.exceptions.GenericServiceException;

@Component
public class StatisticsController implements IStatisticsController {

  @Autowired
  StatisticsService statisticsService;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public Map<TaskStatusDTO, Double> taskDistribution()
    throws GenericServiceException {
    Map<TaskStatus, Double> distribution = statisticsService.taskDistribution();
    Map<TaskStatusDTO, Double> distributionDTO = new HashMap<>();
    for (TaskStatus taskStatus : distribution.keySet()) {
      distributionDTO.put(
        modelMapper.toTaskStatusDTO(taskStatus),
        distribution.get(taskStatus)
      );
    }

    return distributionDTO;
  }
}
