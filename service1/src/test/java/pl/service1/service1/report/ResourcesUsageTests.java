package pl.service1.service1.report;

import org.junit.jupiter.api.*;
import pl.service1.service1.service.sources.report.ResourcesUsageReport;
import pl.service1.service1.service.sources.report.ServicePerformance;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

class ResourcesUsageTests {

    static ResourcesUsageReport report;
    static ServicePerformance testReport;

    @BeforeAll
    static void setUp() throws InterruptedException {
        report = new ResourcesUsageReport();
        report.setDaemon(true);
        report.start();
        TimeUnit.SECONDS.sleep(10);
        testReport = report.getServicePerformance();
    }

    @Test
    @DisplayName("All CPU report values greater or equal than 0")
    void valuesOfCpuReportTest() {
        Collection<Double> cpuReportValues = testReport.cpu().values();

        for (Double cpuReportValue : cpuReportValues) {
            Assertions.assertTrue(cpuReportValue >= 0);
        }
    }

    @Test
    @DisplayName("All memory report values greater or equal than 0")
    void valuesOfMemoryReportTest() {
        Collection<Double> memoryReportValues = testReport.memory().values();

        for (Double memoryReportValue : memoryReportValues) {
            Assertions.assertTrue(memoryReportValue >= 0);
        }
    }

    @Test
    @DisplayName("Collecting CPU report")
    void sizeOfCpuReportTest() {
        int cpuReportSize = testReport.cpu().size();

        Assertions.assertTrue(5 <= cpuReportSize);
    }

    @Test
    @DisplayName("Collecting memory report")
    void sizeOfMemoryReportTest() {
        int memoryReportSize = testReport.memory().size();

        Assertions.assertTrue(5 <= memoryReportSize);
    }
}
