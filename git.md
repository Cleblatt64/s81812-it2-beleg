# Schritte zum eigenen Repository

## Eigenes Repository erstellen
Erzeugen Sie einen eigenen GitHub-Account falls Sie noch keinen haben. Senden Sie das Nutzerkennzeichen per E-Mail an den Modulverantwortlichen oder informieren Sie diesen im Praktikum. Nach dem Erhalt der Bestätigungs-E-Mail sind Sie Mitglied der GitHub-Organisation HTWDD-RN. Falls Sie die E-Email nicht erhalten, können Sie alternativ die Mitgliedschaft auf http://github.com/HTWDD-RN bestätigen.

Erzeugen Sie auf HTWDD-RN ein eigenes **privates** Repository mit einem Namen entsprechend Ihrer S-Nummer und des Modulkürzels (`yyy=[rn|it1|it2|sn|vs]`) in der Form `sXXXX-yyy-beleg` . Fügen Sie als Kommentar Ihren **Namen** hinzu, um die Zuordnung zu erleichtern. Dies können Sie auch nachträglich auf der Website des betreffenden Repositories tuen, wenn Sie beim Code-Tab den Edit-Button benutzen.

Den Namen des zu klonenden Repositories finden Sie auf der Vorlesungswebsite.

Die Fakultät Informatik stellt Ihnen (unabhängig vom RZ) ein Homeverzeichnis bereit, welches unter /user/data/i[a,m,w][0,1,2][0-9]/sXXXXX gemountet ist. Auf dieses können Sie im Labor S311 einfach zugreifen.


**Hinweis:** Anstatt der manuellen Erstellung des Repositories und der Verknüpfung mit dem Aufgaben-Repository im nächsten Schritt könnte man auch die Fork-Funktionalität von Github nutzen um das eigenes Repository zu erstellen, siehe [Github](https://docs.github.com/en/get-started/quickstart/fork-a-repo).  Leider funktioniert dieser Ansatz für unseren Beleg nicht, da wir private Repositories benötigen, welche beim einem Fork nicht angeboten werden.


## Eigenes Repository mit Aufgaben-Repository verknüpfen
Repository lokal klonen  
`git clone https://github.com/HTWDD-RN/"Name des zu klonenden Repositories"`

Wechsel in das angelegte lokale Repository  
`cd "Name des Repositories"`

Umbenennen des Alias des originalen Repositories     
`git remote rename origin htw-upstream`

Anlegen der Verknüpfung (myrepo) mit dem eigenen entfernten Repository  
Syntax: `git remote add [alias] [url]`     
Beispiel: `git remote add myrepo https://github.com/HTWDD-RN/sxxxxx-yyy-beleg`

Aktualisierung des eigenen entfernten Repositories mit aktuellem Branch  
`git push myrepo`

Sie müssen statt eines Passwortes ein Token für den Zugriff nutzen, siehe [hier](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token).  


Testen lässt sich die korrekte Zuordnung mittels: `git remote -vv`
```
htw-upstream	https://nutzer@github.com/HTWDD-RN/RTSP-Streaming (fetch)
htw-upstream	https://nutzer@github.com/HTWDD-RN/RTSP-Streaming (push)
myrepo	      https://nutzer@github.com/HTWDD-RN/sXXXXX-yyy-beleg (fetch)
myrepo	      https://nutzer@github.com/HTWDD-RN/sXXXXX-yyy-beleg (push)
```



## Besonderheiten für das **Labor S311**: 
* Home des Nutzers im Labor ist nicht das Standardhome
* für den Zugriff auf Github ist ein Proxy notwendig, [Beispieldatei](gitconfig-beispiel.txt), diese muss in das S311-Home mit dem Namen `.gitconfig`
* das Repository für den Beleg sollte in das Standardhome, da in dem S311-Home wenig Speicherplatz vorhanden ist
* das Standardhome finden Sie unter: `/user/data/im99/s99999`


## Zusammenfassung
* Im Bild unten ist der geschilderte Prozess zusammengefasst.
* Weitere Informationen zu Nutzung von [Git](git-details.md).


![GitHub-Workflow](/images/git.png)
<!---  <img src="images/git.png" width="100">   --->
