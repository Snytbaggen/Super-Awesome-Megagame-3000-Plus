Milstolpar:

1:  Skapa en spelplan med en fyrkantig plattform som följer musens rörelser (alternativt tangentbordets). 
    Rörelsen ska vara mjuk och inte ha för stor fördröjning gentemot tangentbordet/musens rörelser. 

2:  Rita ut ett rutnät med brickor på spelplanen. Dessa rutor är slumpmässiga med olika egenskaper. Varje 
    egenskap motsvaras av en färg, dock är funktionaliteten av dessa egenskaper implementerad i detta steg. 

3:  Skapa en (fyrkantig) boll i spelplanen som rör sig mjukt och studsar mot kanterna med rätt vinkel. I detta
    steg bryr vi oss inte om kollisionshantering mot brickor eller spelplattan.

4:  Bollen ska nu kunna studsa mot bollarna och spelplattan och behålla rätt vinkel. I nuläget är "död av 
    bollen" inte implementerat.

5:  När bollen träffar en bricka ska en träff registreras, och blocken reagera därefter. Till exempel blir ett 
    block som kräver flera träffar blir förvandlat till ett med lägre grad, och ett oförstörbart händer 
    ingenting med. En poängsumma finns också som uppdateras vid borttagning av block.

6:  När bollen når botten av planen försvinner den, och ersätts med en ny. Denna ska aktiveras och skickas iväg 
    med ett knapptryck eller musklick.

7:  När alla förstörbara block är borta från planen ska en ny spelplan tas fram (slumpad eller förutbestämd) 
    och en ny boll startas utan att kvarvarande liv eller poängsumman påverkas. Vid den här punkten är spelet i
    stort sett helt fungerande.

8:  Bakgrundsmusik till spelet ska spelas och ljudeffekter spelas. De ljudeffekter som ska finnas är när bollen 
    träffar olika objekt, block förstörs, game over och när banan klaras.

9:  Man ska kunna skapa egna spelplaner genom en grafisk editor, där man får upp ett rutnät i samma dimensioner 
    som spelplanen. Man ska kunna välja blocktyp från en drop-down list och sedan placera ut dessa i spelplanen.
    Cellerna i rutnätet ska ändra till den färg som representerar den valda blocktypen vid musklick. Man kan 
    sedan spara ner spelplanen i den lista (eventuellt fil) som spelet använder för att lagra befintliga spelplaner.

10: Olika powerups ska implementeras. Dessa ska falla från en viss typ av block, när det förstörs. Typerna som 
    planeras i skrivande stund är förlängning av spelplattan, fler bollar på plan, bollar som förstör flera block 
    och en powerup som saktar ner alla bollar på plan. 