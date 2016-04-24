# PoLiglot1.0 #
игра про английский язык

## WebSocket /gameplay ##
### Начало игры ###
клиент:
```
ws = new WebSocket("ws://localhost:8080/api/gameplay");
```
сервер:
```
{
  "action":"startGame",
  "user":"kiska123",
  "enemy":"hot_girl"
}
```
### Проверка ответа на правильность###
клиент:
```
ws.send("
  {
    \"action\":\"getWord\"
  }"
);
```
сервер:
```
{
  "action":"getWord",
  "id":1,
  "shuffleWord":"tac"
}
```
клиент:
```
ws.send("
  {
    \"action\":\"checkWord\",
    \"id\":1,
    \"word\":\"cat\"      //слово, которое ввел игрок
  }"
);
```
сервер:
```
{
  "action":"checkWord",
  "answer":true,          //правильное ли слово, которое ввел игрок
  "right":"cat",
  "myScore":10,
  "enemyScore":20
}
```
### Конец игры ###
сервер: 
```
{
  "action":"finishGame",
  "win":false,
  "myScore":10,
  "enemyScore":20,
  "best":true            //наилучшей ли это результат игрока
}
```
если ничья:
```
{
  "action":"finishGame",
  "equality":true,
  "myScore":10,
  "enemyScore":10,
  "best":true
}
```


##Installing##
<p>Install maven</p>
<p>cd ../2016-02-Poliglot1.0/Project</p>
<p>mvn clean install</p>
<p>mvn frontend:grunt</p>
<p>http://localhost:8080/</p>


