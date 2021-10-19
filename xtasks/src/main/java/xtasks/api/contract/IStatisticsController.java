package xtasks.api.contract;

import java.util.Map;
import xtasks.backend.services.StatisticsService;
import xtasks.backend.services.exceptions.GenericServiceException;

public interface IStatisticsController {
  /**
   * see {@link StatisticsService#taskDistribution}
   */
  Map<TaskStatusDTO, Double> taskDistribution() throws GenericServiceException;
}
