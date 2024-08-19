package pl.service2.service2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Request;
import okhttp3.Response;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.service2.service2.service.sources.JsonClass.Json;
import pl.service2.service2.service.sources.WrongStatusCode;
import pl.service2.service2.service.sources.parents.ServiceExtended;
import pl.service2.service2.service.sources.report.ServicePerformance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class Service2 extends ServiceExtended {

    //klasy pomocnicze
    private static final Logger log = LoggerFactory.getLogger(Service2.class);

    private Json[] listOfJsons;

    //parametry
    private String jsonGenUrl;
    private String reportUrl;
    private final Class<Json> jsonClass = Json.class;
    private final List<String> jsonKeys = setJsonKeys();
    private final String[] mathChars = new String[]{"*", "/", "+", "-", "%", "(", ")"};
    private final String[] basicHeads = new String[]{"_type", "_id", "name", "type", "latitude", "longitude"};

    public Service2() {
        homeUrl = "http://172.20.1.1:5000/";
        resourcesUsageReport.start();
        constructorSetter();
    }

    public Service2(String homeUrl) {
        this.homeUrl = homeUrl;
        resourcesUsageReport.start();
        constructorSetter();
    }

    //to chyba można prościej
    public HttpStatus newJsons(int size) {

        Request newJsonsRequest = new Request.Builder().url(jsonGenUrl + size).build();

        numOfRequest++;
        double responseTime;
        long startTime = System.nanoTime();
        HttpStatus responseCode;
        StringBuilder newJsonLog = new StringBuilder().append("Method: newJson | ");


        try (Response response = serviceConnection.newCall(newJsonsRequest).execute()) {

            responseTime = (System.nanoTime() - startTime) / 1_000_000.0;

            if (response.code() == statusOK && response.body() != null) {

                listOfJsons = new ObjectMapper().readValue(response.body().string(), new TypeReference<>(){});
                numOfJsons = size;
                responseCode = HttpStatus.OK;
                log.info(newJsonLog.append("Success | Size: ").append(size).toString());

            } else {
                throw new WrongStatusCode(response.code());
            }

        } catch (Exception e) {

            numOfJsons = -1;
            responseTime = -1;
            responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(newJsonLog.append(e.getMessage()).toString());
        }

        updateRequestsStats(responseTime);
        return responseCode;
    }

    public String select() {
        StringBuilder logString = new StringBuilder().append("Method: basic | ");

        if (numOfJsons <= 0) {
            log.error(logString.append("Forbidden | No jsons").toString());
            return "";
        }

        return selectedHeadsToCsvLike(basicHeads, logString);
    }

    public String select(String headsStr) {

        StringBuilder logString = new StringBuilder().append("Method: select | ");

        if (numOfJsons <= 0) {
            log.error(logString.append("Forbidden | No jsons").toString());
            return "";
        }

        if (Objects.equals(headsStr, "")) {
            log.error(logString.append("Forbidden | No selected heads").toString());
            return "";
        }

        return selectedHeadsToCsvLike(headsStr.split(","), logString);
    }

    public String calculate(String[] expressions) {

        HashMap<String, Object[]> calculatedValues = new HashMap<>();
        HashMap<String, Object[]> valuesOfUsedFields;
        String[] partsOfExpression;
        List<String> headsInExpression;
        StringBuilder preparedExpression;
        StringBuilder logString = new StringBuilder().append("Method: calculate | ");
        String CSVLikeAnswer = "";

        for (String expression : expressions) {

            calculatedValues.put(expression, new Object[numOfJsons]);
            partsOfExpression = prepareExpression(expression);
            headsInExpression = getFieldNamesFromExpression(partsOfExpression);
            valuesOfUsedFields = extractHeads(headsInExpression.toArray(new String[0]));

            for (int i = 0; i < numOfJsons; i++) {

                preparedExpression = new StringBuilder();

                for (String part : partsOfExpression) {

                    if (!headsInExpression.contains(part)) preparedExpression.append(part);
                    else preparedExpression.append(valuesOfUsedFields.get(part)[i]);

                }

                try {
                    Expression toCalculate = new ExpressionBuilder(preparedExpression.toString()).build();
                    calculatedValues.get(expression)[i] = toCalculate.evaluate();

                } catch (Exception e) {

                    log.warn(logString.append("Forbidden expression or value: ").append(preparedExpression).toString());
                    calculatedValues.get(expression)[i] = "n/d";
                }
            }
        }

        try{
            CSVLikeAnswer = HashMapToCSVLikeString(calculatedValues);
            log.info(logString.append("Success").toString());

        } catch (Exception e) {

            logString.append(e.getMessage());
            log.error(logString.toString());
        }

        return CSVLikeAnswer;
    }

    public ServicePerformance bridge() {

        Request reportRequest = new Request.Builder().url(reportUrl).build();
        ServicePerformance service1Performance = null;
        ServicePerformance repTmp;

        try (Response response = serviceConnection.newCall(reportRequest).execute()) {

            if (response.code() == statusOK && response.body() != null) {
                repTmp = new ObjectMapper().readValue(response.body().string(), new TypeReference<>(){});
                service1Performance = new ServicePerformance(repTmp.cpu(), repTmp.memory(), requestsStats);

            } else {

                throw new WrongStatusCode(response.code());
            }


        } catch (Exception e) {

            log.error("Method: bridge | {}", e.getMessage());
        }

        return service1Performance;
    }

    private String selectedHeadsToCsvLike(String[] selectedHeads, StringBuilder logString) {
        String CSVLikeAnswer = "";

        HashMap<String, Object[]> selectedColumns = extractHeads(selectedHeads);

        try{
            CSVLikeAnswer = HashMapToCSVLikeString(selectedColumns);
            log.info(logString.append("Success").toString());

        } catch (Exception e) {

            log.error(logString.append(e.getMessage()).toString());
        }

        return CSVLikeAnswer;
    }

    private HashMap<String, Object[]> extractHeads(String[] heads) {

        HashMap<String, Object[]> extractedHeads = new HashMap<>();
        StringBuilder logString = new StringBuilder().append("Method: extractHeads | ");

        for (String head : heads) {

            try {
                Method getMethod = jsonClass.getMethod(head);


                extractedHeads.put(head, new Object[numOfJsons]);

                for (int index = 0; index < numOfJsons; index++) {

                    extractedHeads.get(head)[index] = getMethod.invoke(listOfJsons[index]);

                }

            } catch (NoSuchMethodException e) {

                log.error(logString + "Method '{}' not found", head);

                HashMap<String, Object[]> errorMap = new HashMap<>();
                Object[] errorMessage = new String[]{"Method " + head + " not found"};
                errorMap.put("Error", errorMessage);
                return errorMap;

            } catch (Exception e) {

                log.error(logString.append(e.getMessage()).toString());

                HashMap<String, Object[]> errorMap = new HashMap<>();
                Object[] errorMessage = new String[1];
                errorMessage[0] = e.getMessage();
                errorMap.put("Error", errorMessage);
                return errorMap;
            }
        }

        return extractedHeads;
    }

    private String HashMapToCSVLikeString(HashMap<String, Object[]> map) {
        StringBuilder CSVLikeString = new StringBuilder();
        StringBuilder row = new StringBuilder();
        List<String> keySet = map.keySet().stream().toList();

        for (String key : keySet) {
            row.append(key).append(",");
        }
        CSVLikeString.append(row.substring(0, row.length()-1)).append("<br/>");

        for (int i=0; i<map.get(keySet.get(0)).length; i++) {

            row = new StringBuilder();

            for (String key : keySet) {
                row.append(map.get(key)[i]).append(",");
            }
            CSVLikeString.append(row.substring(0, row.length()-1)).append("<br/>");
        }

        return CSVLikeString.toString();
    }

    private String[] prepareExpression(String expression) {
        for (String character : mathChars) {
            expression = expression.replace(character, " " + character + " ");
        }

        return expression.split(" ");
    }

    private List<String> getFieldNamesFromExpression(String[] partsOfExpression) {

        List<String> fieldNames = new ArrayList<>();

        for (String part : partsOfExpression) {

            if (jsonKeys.contains(part)) fieldNames.add(part);

        }

        return fieldNames;
    }

    private List<String> setJsonKeys() {
        Field[] fields = jsonClass.getDeclaredFields();
        List<String> fieldsList = new ArrayList<>(Arrays.stream(fields).map(Field::getName).toList());

        for (Field field : fields) {

            if (!field.getType().isPrimitive() && !field.getType().equals(String.class)) {

                Class<?> fieldType = field.getType();
                Field[] subFields = fieldType.getDeclaredFields();
                fieldsList.addAll(Arrays.stream(subFields).map(Field::getName).toList());
            }
        }

        return fieldsList;
    }

    @Override
    protected void setEndPoints() {
        jsonGenUrl = homeUrl + "generate/json/";
        reportUrl = homeUrl + "report";
    }
}
