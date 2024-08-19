package pl.service2.service2.service.sources.parents;

import lombok.Getter;
import pl.service2.service2.service.sources.report.ResourcesUsageReport;

@Getter
public class ServiceBasic {

    protected final ResourcesUsageReport resourcesUsageReport = setResourcesUsageReport();

    private ResourcesUsageReport setResourcesUsageReport() {
        ResourcesUsageReport resourcesUsageReport = new ResourcesUsageReport();

        resourcesUsageReport.setDaemon(true);

        return resourcesUsageReport;
    }
}
