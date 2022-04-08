W celu uruchomienia aplikacji należy uruchomić:

1. Metodę main w ServerApp - aplikacja serwera czatu
2. Metodę main w FileSendApp - pozwala na wysyłanie plików przez serwer
3. Metodę main w FileDownloadApp - pozwala na pobieranie plików przez serwer
4. Metodę main w ClientApp - aplikacja kliencka czatu

Po uruchomienia klienta aplikcja prosi o podanie imienia. Następnie wita się z użytkownikiem
i podpowiada komendę (**help), która wypisuje dostępne komendy z ich opisami.

Aplikacja zapisuje pliki przesyłane przez użytkownika w folderze file\[nazwa pokoju, w której znajduje się użytkownik].
Użytkownik może pobrać pliki tylko z pokoju, w którym się znajduje.
W takcie wysyłania/pobierania pliku można kontynuować konwersację.

Historia konwersacji danego pokoju zapisuje się na bieżąco w wyżej wymienionym folderze, w pliku history.txt.

Zajęte porty: 5000, 6000, 7000. Można je zmienić w klasie Conf.
