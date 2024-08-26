>>> Polecenie

Trzy serwisy.

1. Serwis który zwraca pod adresem ‘/generate/json/{size}’ listę jsonow o wskazanym rozmiarze
oraz o strukturze poniżej z losowymi wartościami.
np.
{ _type: "Position", _id: 65483214, key: null, name: "Oksywska", fullName: "Oksywska,
Poland", iata_airport_code: null, type: "location", country: "Poland", geo_position:
{ latitude: 51.0855422, longitude: 16.9987442 }, location_id: 756423, inEurope: true,
countryCode: "PL", coreCountry: true, distance: null }

2. Serwis który pobiera dane z pierwszego i konwertuje go do csv. Pierwszy endpoint który
zawsze zwraca pobrane dane w formacie ‘type, _id, name, type, latitude, longitude’. Drugi
endpoint który zwraca pobrane dane w danej strukturze csv czyli podajemy w zapytaniu ‘ id,
latitude, longitude’ i oczekujemy że zwróci taki wynik ‘ 65483214, 51.0855422, 16.9987442 ’.
Trzeci endpoint który na wejściu oczekuje definicji prostych operacji matematycznych w formie
listy np ‘ latitude*longitude,sqrt(location_id)’ i w wyniku zwróci ‘ 3.0052538 , 869.7258188 ’

3. Serwis który wykonuje zapytania na drugim i wyświetla proste raporty dotyczące wydajności.
Raport powinien zawierać informacje takie jak użycie procesora,pamięci w czasie dla każdego z
poprzednich serwisów oraz czas zapytań http pomiędzy serwisami 3->2->1.
Raport na 1k,10k,100k wygenerowanych jsonow.

>>> Start

Struktura katalogów jest przygotowana do uruchomienia poprzez "docker-compose up --build".
Po uruchomieniu, należy wejść na adres podawany przez "Serwis3" (http://localhost:5003).
Na stronie wybieramy intersującą nas opcje:
- Pole do wprowadzenia ilość jsonów znajduje się na samej górze oraz przycisk zatwierdzający wybór "new".
- Przycisk wykonujący zapytanie o podstawowe wartości jsonów "Basic".
- Chceckboxy do wyboru własnych wartości oraz przycisk zatwierdzający wybór.
- Pole tekstowe do wprowadzania prostych obliczeń matematycznych rozdzielonych przecinkiem oraz przycisk zatwierdzający wybór.

Raporty dostępne są pod enpointem "/report". Wykres dla zużycia procesora oraz pamięci i tabelka dla czasu wykonania zapytań dla serwisów nr. 1 oraz nr. 2.

>>> Testy

Testy serwisów nr.2 i nr.3 są możliwe po stowrzeniu obrazów dla kontenerów testowych. Potrzebne pliki znajdują się w katalogach ServicesJava\TestContainers\Test4Service2 oraz ..\Test4Service3.
Zalecane polecenia:

- docker build . -t servis1wraith:1.0 (w folderze Test4Service2)
- docker build . -t servis2wraith:1.2 (w folderze Test4Service3)

>>> Technologie

- Java 17 1.8.0_51
- SpringBoot 3.3.2
- Apache Maven 3.9.6
- Lombok 1.18.34
- Test Containers 1.20.1
- okhttp 4.12.0
- exp4j 0.4.8

- Docker 26.1.4
- Docker Compose 2.27.1

Pozostałe, użyte w projekcie biblioteki zostały pobrane przez SpringBoot.

