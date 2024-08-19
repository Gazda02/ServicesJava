package pl.service3.service3.service.sources.parents;

import lombok.Getter;
import pl.service3.service3.service.sources.report.ResourcesUsageReport;

@Getter
public class ServiceBasic {

    protected final ResourcesUsageReport resourcesUsageReport = setResourcesUsageReport();

    private ResourcesUsageReport setResourcesUsageReport() {
        ResourcesUsageReport resourcesUsageReport = new ResourcesUsageReport();

        resourcesUsageReport.setDaemon(true);

        return resourcesUsageReport;
    }
}
