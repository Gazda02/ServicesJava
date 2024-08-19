package pl.service1.service1.service.sources.report;

import com.sun.management.OperatingSystemMXBean;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Klasa odpowiedziala za gromadzenie danych o zużyciu zasobów sprzętowych, z możliwością wyboru odstępów czasowych[sek] (domyślnie 1s).
 * Zebrane wartości są w formie { *czas*: { "RAM": *wartość*, "CPU": *wartość*}}
 * Zbieranie inforamcji działa w osobnym wątku (extends Thread)
 */
@Getter
public class ResourcesUsageReport extends Thread{

    private final ServicePerformance servicePerformance = new ServicePerformance(new HashMap<>(), new HashMap<>(), new HashMap<>());
    private static final Logger log = LoggerFactory.getLogger(ResourcesUsageReport.class);

    @Setter
    private int delay = 1;

    public ResourcesUsageReport(){}

    @Override
    public void run(){

        double ram_usage;
        double cpu_usage;
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        log.info("Start of making a resource usage report | Delay: {} sec", delay);

        while(true){

            try {
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException e) {
                log.error("Sleep interrupted");
            }

            //zczytanie zużycia RAMu
            ram_usage = (double) (osBean.getTotalMemorySize() - osBean.getFreeMemorySize())
                    / osBean.getTotalMemorySize();
            servicePerformance.memory().put(LocalTime.now().toString(), ram_usage * 100);

            //zczytanie zużycia CPU
            do { cpu_usage = osBean.getCpuLoad(); }
            while (cpu_usage < 0);
            servicePerformance.cpu().put(LocalTime.now().toString(), cpu_usage * 100);
        }
    }
}
