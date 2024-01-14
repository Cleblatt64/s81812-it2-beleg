# Aufgabenstellung RTSP-Streaming
Die Aufgaben beziehen sich auf den Beleg Videostreaming für das Modul Internettechnologien 2.

## Aufgaben

### 0. Vorarbeiten
1. Sie clonen das Projekt nach Anleitung aus [Git](git.md).
2. Sie erstellen die "leeren" Klassen `Rtsp`, `RTPpacket` und `FECHandler` und leiten diese aus den abstrakten Klassen `RtspDemo`, `RTPpacketDemo` und `FECHandlerDemo` ab (Stichwort `extends`).  Das Projekt sollte danach kompilierbar und ausführbar sein.  
Unter einigen IDEs z.B. IntelliJ können Sie die Klassenrümpfe automatisch erstellen lassen mittels: Generate Contructors sowie implement Methods
3. Sie konfigurieren die Kommandozeilenparameter für Client und Server wie in der [Projektbeschreibung](Projektbeschreibung.md#2.-programmstart) beschrieben.
4. Sie erstellen in Ihrem Gitverzeichnis ein Unterverzeichnis mit dem Namen `videos` und legen in dieses das Beispielvideo `htw.mjpeg`, siehe Praktikumsdateien auf der HTW-IT2-Homepage.


### 1. RTSP-Protokoll: Client-Methoden
Die gesamte RTSP-Funktionalität für Client und Server befindet sich in der abstrakten Klasse `RtspDemo` und der von Ihnen abzuleitenden Klasse `Rtsp`.
Programmieren Sie die Klasse `Rtsp` entsprechend der in der Projektbeschreibung und den Kommentaren im Quelltext der abstrakten Klasse `RtspDemo` gegebenen Hinweisen.

1. Buttonhandler für alle Methoden ausprogrammieren, als Beispiel siehe den Handler für die setup-Methode und [Hinweise zu Zuständen](Projektbeschreibung.md#zustände-des-clients).
2. Ausprogrammierung der Methode `send_RTSP_request()`. Hier muss über den vorhandenen Stream `RTSPBufferedWriter` der komplette RTSP-Request für alle möglichen Methoden als String zusammengebaut und verschickt werden. Orientieren Sie sich an der beispielhaften [RTSP-Kommunikation](Projektbeschreibung.md#beispiel).
3. Nach Ihren Arbeiten können Sie die RTSP-Funktionalität testen indem Sie die Konsolenausgaben inspizieren.


### 2. SDP-Protokoll
Ergänzen Sie die RTSP-Methode DESCRIBE in der Klasse `Rtsp` anhand der Beispiele aus [RFC 2326](https://www.ietf.org/rfc/rfc2326.txt) und [RFC 2327](https://www.ietf.org/rfc/rfc2327.txt).
Überschreiben Sie dazu die bereits vorhandene Methode `getDescribe()` aus der Klasse `RtspDemo` in der Klasse `Rtsp`.
Die Serverantwort muss im Client nicht ausgewertet werden. Die Anzeige der Antwort in der Konsole des Clients genügt.

Es ist ausreichend, sich bei der DESCRIBE-Methode auf das Beispielvideo zu beziehen und die Antwort auf dem Server statisch zu hinterlegen. 
Ausgewertet werden die u.a. die Parameter `framerate` und `range`.

### 3. RTP-Protokoll
Programmieren Sie die Methode setRtpHeader() der Klasse `RTPpacket` entsprechend der Projektbeschreibung und den Kommentaren im Quelltext der abstrakten Klasse gegebenen Hinweisen.
Nach dem Setzen des korrekten RTP-Headers sollte das Demovideo abspielbar sein. Im Fehlerfall kann es hilfreich sein, mittels Wireshark den Inhalt der übertragenen RTP-Pakete zu inspizieren. Eventuell ist auch der zur Verfügung gestellte Paketmitschnitt hilfreich.


### 4. Auswertung der Fehlerstatistiken ohne Fehlerkorrektur
Sie können an der GUI des Servers eine Paketfehlerwahrscheinlichkeit einstellen und damit Netzwerkfehler simulieren. Probieren Sie verschiedene Einstellungen aus und betrachten Sie das Ergebnis in der Videoanzeige. 
Ab welcher Paketfehlerwahrscheinlichkeit wird die Videoanzeige spürbar beeinträchtigt?

Das RTP-Streaming ist z.Z. so konfiguriert, dass ein JPEG-Bild in ein einziges RTP verpackt wird. 
Berechnen die Wahrscheinlichkeit für den Verlust eines Bildes in Abhängigkeit von der Kanalverlustrate, wenn pro Bild 2 oder 5 RTPs versendet werden.
Von einem Bildverlust ist dabei auszugehen, wenn mindestens ein RTP der RTPs für ein Bild fehlt.
Nutzen Sie zur grafischen Darstellung das Programm Gnuplot. Eine Demodatei befindet sich im Projektverzeichnis `statistics`. 


### 5. Implementierung des FEC-Schutzes
Implementieren Sie einen FEC-Schutz gemäß [RFC 5109](https://www.ietf.org/rfc/rfc5109.txt).
Der Server mit FEC-Schutz soll kompatibel zu Clients ohne FEC-Verfahren sein! Nutzen Sie dazu das Feld Payloadtype des RTP-Headers (PT=127 für FEC-Pakete).

Um nicht die komplette FEC-Funktionalität selbst entwickeln zu müssen, werden Ihnen zwei Klassen bereit gestellt:
1. [FECpacket](src/FECpacket.java): dies ist eine aus RTPpacket abgeleitete Klasse mit der erweiterten Funktionalität für das Handling von FEC-Paketen (vollständig implementiert)
2. [FecHandler](src/Fechandler.java): diese Klasse ist zuständig für die server- und clientseitige FEC-Bearbeitung unter Nutzung von FECpacket (teilweise implementiert)
   * Server: Kombination mehrerer Medienpakete zu einem FEC-Paket
   * Client: Jitterpuffer für empfangene Medien- und FEC-Pakete, Bereitstellung des aktuellen Bildinhaltes in Form einer Liste von RTP-Paketen mit gleichem TimeStamp.

Die Fehlerkorrektur im FecHandler ist noch zu implementieren. Dazu ist die vorhandene Architektur zu analysieren und die abstrakten Methoden sind auszuprogrammieren.
Eine Übersicht über die relevanten Datenstrukturen und ein Beispiel ist hier zu finden [FEC-Diagramme](https://www2.htw-dresden.de/~jvogt/it2/fec-diagramme.html)

#### Architektur der Paketverarbeitung
##### Server
* der Server steuert die Verarbeitung im vorhandenen Timer-Handler
* Nutzdaten erstellen und speichern: `RtpHandler.jpegToRtpPacket()`
* Nutzdaten senden
* Prüfung auf Erreichen der Gruppengröße: `RtpHandler.isFecPacketAvailable()`
* nach Ablauf des Gruppenzählers berechnetes FEC-Paket entgegennehmen und senden: `RtpHandler.createFecPacket()`
* Kanalemulator jeweils für Medien- und FEC-Pakete aufrufen: `sendPacketWithError()`

##### Client
* Der Client nutzt getrennte Timer-Handler für den Empfang der Pakete und für das periodische Anzeigen der Bilder (keine Threads notwendig).
* Pakete empfangen per Timer
* Pakete im Jitterpuffer speichern: `RtpHandler.processRtpPacket()`
* Statistiken aktualisieren
* zur richtigen Zeit (Timeraufruf) das nächste Bild anzeigen: `RtpHandler.nextPlaybackImage()`
    * Timer läuft standardmäßig mit 25Hz oder Abspielgeschwindigkeit des Videos, wenn diese per SDP übermittelt
* Verzögerung des Starts des Abspielens (ca. 2s), um den Jitterpuffer zu füllen

##### RtpHandler
* Server
    * Registrierung eines RTP-Paketes im FecHandler bei dessen Erstellung
    * Abruf fertiggestellter FEC-Pakete über den FeCHandler
* Client
    * Speicherung ankommender RTP-Pakete getrennt nach PayloadType (JPEG-Pakete im RtpHandler, FEC-Pakete im FecHandler)
    * Bei Anfrage des nächsten Bildes Generierung einer Liste aller RTP-Pakete für dieses Bild (gleicher Timestamp)
    * Überprüfung der Korrektur für fehlende RTP-Pakete per `FecHandler.checkCorrection()` und ggf. Korrektur über `FecHandler.correctRTP()`

##### FecHandler
* Generierung einer Liste aller betroffenen RTP-Pakete für jedes FEC-Paket
* Speicherung der Sequenznummer des FEC-Packets und der Liste aller betroffenen RTP-Pakete für jedes RTP-Paket in zwei HashMaps (fecNr, fecList)
* periodisches Löschen alter nicht mehr benötigter Einträge im Jitterpuffer

##### FECpacket
* Ableitung aus vorhandenem RTPpacket
* Sender: Konstruktor zur Generierung eines FEC-Objektes aus Media-RTPs
* Empfänger: Konstruktur zur Generierung eines FEC-Objektes aus einem empfangenen FEC-RTP
* getRtpList: Ermittlung aller in einem FEC involvierten Media-RTPs
* getPacket: Holt komplettes FEC-Paket als Bytearray
* addRtp: fügt ein Media-RTP zum FEC-Objekt hinzu, inklusive aller notwendigen Berechnungen
* getLostRtp: generiert das verlorene Media-RTP aus den vorhandenen mittels addRtp hinzugefügten RTPs

#### Debugging
Es ist relativ unwahrscheinlich, dass das Programm auf Anhieb fehlerfrei funktioniert. Deshalb ist es wichtig, ein Konzept für die Fehlersuche zu entwickeln.
Hier einige Tipps für die Fehlersuche:
* Anzeige von Statusinformationen analog zu printheaders() des RTPpackets()
* Anzeige der ersten Bytes des Payload auf Sender und Empfänger
* prüfen des Senders auf korrekte Pakete
* Einstellung eines festen Seeds des Kanalsimulators für wiederholbare Versuche
* Test ohne bzw. mit Fehlerkorrektur
* Test der Anzahl verlorener / wiederhergestellter Pakete auf Plausibilität


### 6. Analyse der Leistungsfähigkeit des implementieren FEC-Verfahrens
#### Parameterwahl
Finden Sie den optimalen Wert für k bei einer Kanalverlustrate von 10%. Optimal bedeutet in diesem Fall eine subjektiv zufriedenstellende Bildqualität bei geringstmöglicher Redundanz.

#### Bestimmung der theoretisch zu erwartenden Verlustraten

Tragen Sie die mittels Messung zu gewinnenden Paketverlustwahrscheinlichkeiten (Restfehler) für verschiedene Gruppengrößen (k=2, 3, 6, 12, 24, 48) in dem bereits vorhandenen Gnuplot-Diagramm auf.

Versuchen Sie, mathematisch die Paketverlustwahrscheinlichkeit für die obigen Gruppengrößen zu bestimmen und ebenfalls grafisch darzustellen.
Gehen Sie dabei von folgenden hypothetischen Übertragungsmodies aus: 1 RTP / Bild, 2 RTPs / Bild und 5 RTPs / Bild.

Für die eigentliche Berechnung können Sie statt Gnuplot auch R oder ein anderes Tool nutzen.
Diskutieren Sie eventuelle Unterschiede der praktisch und theoretisch ermittelten Ergebnisse.


Für diese Aufgabe unterstützt Sie die Statistik am Empfänger mit dem Werten:
1. aktuelle Puffergröße
2. letzte empfangene Sequenznummer
3. Anzahl erhaltener / verlorener Medienpakete + prozentuale Angabe verlorener Medienpakete
4. Anzahl korrigierter / unkorrigierbarer Medienpakete + prozentuale Angabe unkorrigierbarer Medienpakete
5. Abspielzähler (Pakete / Bilder)
6. verlorene Bilder


### 7. Kompatibilität des Demoprojektes
Prüfen Sie die Kompatibilität des Clients und Servers mit dem VLC-Player und versuchen Sie eventuelle Probleme zu analysieren. Dokumentieren Sie die Ergebnisse.


### 8. Vorschläge
Manchen Sie konkrete Vorschläge zur Verbesserung des Belegs.


### Hinweis 
Falls Sie ein anderes Video nutzen wollen, ist dieses in das MJPEG-Format zu konvertieren.
Eine Umcodierung zu MJPEG kann zum Beispiel mittels FFMPEG oder VLC-Player erfolgen. Eventuell müssen Sie die Auflösung des Videos verringern, damit die Bilder jeweils in ein UDP-Paket passen.

`ffmpeg -i test.mp4 -vcodec mjpeg -q:v 10 -huffman 0 -r 10 -vf scale=720:-1 -an test.mjpeg`



## Lernaspekte des Belegs
* Kommunikationsprotokolle
  * Internetprotokolle, RFCs
* Programmierung
  * JAVA-Programmierung
  * Debugging
* Softwarearchitektur
  * Entwurfsmuster, FSM
* Mathematik/Stochastik
  * Fehleranalyse
* Grundlagen der Informatik
  * Datenformate
  * Serialisierung
* Betriebssysteme
  * Shellscripte
  * Plattformunabhängigkeit


## Literatur
* Real Time Streaming Protocol (RTSP)                   [RFC 2326](http://www.ietf.org/rfc/rfc2326.txt)
* SDP: Session Description Protocol                     [RFC2327](http://www.ietf.org/rfc/rfc2327.txt)
* RTP: A Transport Protocol for Real-Time Applications  [RFC 3550](http://www.ietf.org/rfc/rfc3550.txt)
* RTP Payload Format for JPEG-compressed Video          [RFC 2435](http://www.ietf.org/rfc/rfc2435.txt)
* RTP Profile for Audio and Video Conferences with Minimal Control  [RFC 3551](http://www.ietf.org/rfc/rfc3551.txt)
* RTP Payload Format for Generic Forward Error Correction  [RFC 5109](http://www.ietf.org/rfc/rfc5109.txt)
* Reed-Solomon Forward Error Correction (FEC) Schemes   [RFC 5510](http://www.ietf.org/rfc/rfc5510.txt)
* JPEG-Format [Link](https://de.wikipedia.org/wiki/JPEG_File_Interchange_Format)
* Diplomarbeit Karsten Pohl "Demonstration des RTSP-Videostreamings mittels VLC-Player und einer eigenen Implementierung"  [pdf](https://www2.htw-dresden.de/~jvogt/abschlussarbeiten/Pohl-Diplomarbeit.pdf)
* Diplomarbeit Elisa Zschorlich "Vergleich von Video-Streaming-Verfahren unter besonderer Berücksichtigung des Fehlerschutzes und Implementierung eines ausgewählten Verfahrens" [pdf](https://www2.htw-dresden.de/~jvogt/abschlussarbeiten/zschorlich-diplomarbeit.pdf)
