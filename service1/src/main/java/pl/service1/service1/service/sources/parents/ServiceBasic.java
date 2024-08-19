package pl.service1.service1.service.sources.parents;

import lombok.Getter;
import pl.service1.service1.service.sources.report.ResourcesUsageReport;

/**
 * Klasa bazowa wszystkich serwisów
 */
@Getter
public class ServiceBasic {

    //osobny wątek zbierający inforamcje o zużyciu zasobów
    protected final ResourcesUsageReport resourcesUsageReport = setResourcesUsageReport();

    /**
     * Ustawienie opcji wątku
     *
     * @return obiekt zbierający informacje z zużyciu zasobów
     */
    protected ResourcesUsageReport setResourcesUsageReport() {
        ResourcesUsageReport resourcesUsage = new ResourcesUsageReport();

        resourcesUsage.setDaemon(true);

        return resourcesUsage;
    }
}
