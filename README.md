<h3>Pozadavky:</h3>
<div>
<h5>Vyber vhodne technologie a jazyka: Java/SpringBoot, Java, C#, …</h5>
<br/>
    <p>Vyuzivame Java/SpringBoot(slozka "backend") a React js slozka(slozka "frontend")</p>
<hr/>
<h5>vyuziti spolecne DB (relacni nebo grafova)</h5>
<br/>
<p>
Vyuzivame Postgres, ktery nam nabizi heroku https://data.heroku.com/
</p>
<hr/>
<h5>Vyuziti cache (napriklad Hazelcast)</h5>
<br/>
<p>Vyuzivame Hazelcast pro entity "Topic" "Deck" "Card" ve slozce "backend"</p>
<hr/>
<h5>Vyuziti messaging principu (Kafka nebo JMS)</h5>
<br/>
<p>Vyuzivame JMS, ktery loguje ruzne requesty, ktere prichazi na server (slozka "backend")</p>
<hr/>
<h5>Aplikace bude zabezpecena pomoci bud basic authorization nebo pomoci OAuth2</h5>
<br/>
<p>Mame obycejnou authorizace, ktera funguje pres rest api s vyuzitim JWT (JSON Web Token) library for Java</p>
<hr/>
<h5>Vyuziti Inteceptors (alespon jedna trida) - napriklad na logovani (prijde request a zapiseme ho do logu)</h5>
<br/>
<p>Vyuzivame Interceptors pro logovani a pro kontrolu requestu, ktere chodi na server</p>
<hr/>
<h5>Vyuziti jedne z technologie: SOAP, REST, graphQL, Java RMI, Corba, XML-RPC</h5>
<br/>
<p>Cela aplikace funguje pomoci REST API   </p>
<hr/>
<h5>Nasazeni na produkcni server napriklad Heroku</h5>
<p>https://gitlab.fel.cvut.cz/filatrom/nss_semestralka_flashcards/-/blob/master/Deploy_guide.pdf</p>
<br/>
<hr/>
<h5>inicializacni postup (jak aplikaci deploynout, kde jsou zakladni data do nove DB typu admin apod)</h5>
<p>
    inicializacni postup -> viz. https://gitlab.fel.cvut.cz/filatrom/nss_semestralka_flashcards/-/blob/master/Deploy_guide.pdf <br/>
    Momentalne aplikace bezi tady(heroku) https://nssflashcards.herokuapp.com/<br/>
    Prihlaseni:
    <table>
      <tr>
        <th>role</th>
        <th>username</th>
        <th>password</th>
      </tr>
      <tr>
        <td>ROLE_ADMIN</td>
        <td>admin</td>
        <td>Yev45823</td>
      </tr>
      <tr>
        <td>ROLE_USER</td>
        <td>user</td>
        <td>12345678</td>
      </tr>
    </table>
</p>
<br/>
<hr/>
<h5>Vyuziti elasticsearch</h5>
<br/>
<p>Pracujeme s elasticsearch pomoci dao vrstvy ve slozce backend/.../esdao a rest api ve slozce backend/.../rest</p>
<hr/>
<h5>Pouziti alespon 5 design patternu (musi davat smysl :) )</h5>
<br/>
<p>
<ul>
  <li>
        Builder 
        <p>viz. backend/.../model</p>
  </li>
  <li>
        Facade
        <p>viz. backend/.../service/SearchFacade.java</p>
  </li>
  <li>
        Dao/Dto/Repository
        <p>viz. backend/.../esdao</p>
  </li>
    <li>
        Object pool
        <p>cache</p>
  </li>
<li>
        IoC
  </li>
  <li>
        Chain of responsibility
  </li>
</ul>
</p>
<hr/>
<h5>Za kazdeho clena tymu 2 UC (use cases - aby SW nebyl trivialni)</h5>
<br/>
<p>Aplikace obsahuje nasledujici use casy viz. https://docs.google.com/document/d/1T7bbh0ijaBTdrO8DwIDK1L_-_Qj0sbewWAFZaArEL3E/edit?usp=sharing</p>
<hr/>
</div>
<h3>Dokumentace:</h3>
https://docs.google.com/document/d/1kx-kQpdytpP5z9Q-YZ9YTitNzIqhB9G442HSeFbVmIY/edit