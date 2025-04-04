package net.todoApplication.services.interfaces;

import java.util.Date;
import java.util.Map;

public interface ReportService {

    Map<String, Object> generateUserActivityReport(Date startDate, Date endDate);
    Map<String, Object> generateCompletionRateReport(String userId, Date startDate, Date endDate);
    Map<String, Object> generateCategoryDistributionReport(String userId);
}
