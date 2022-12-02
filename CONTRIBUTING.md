## Порядок внесения правок
Сборка проекта делается из ветки master, поэтому она всегда должна быть рабочей. В нее можно вносить какие то срочные исправления ошибок. 
Для внесения нового функционала такой подход:
1. Заводится [тикет](https://gitlab.aplana.com/aplanaframework/Chameleon/issues)
2. Добавляется ветка с названием feature/n (n-номер тикета) [сюда](https://gitlab.aplana.com/aplanaframework/Chameleon/branches) (создается на основе ветки мастер)
3. Когда работа завершена делается [merge request](https://gitlab.aplana.com/aplanaframework/Chameleon/merge_requests) на Дильдина Николая
4. После ревью код вливается в ветку мастер
5. После мержа в ветке мастер будет запущена [сборка]((http://jenkins.aplana.com:8080/job/Chameleon/job/ChameleonBuild)) и [публикация проекта](http://jenkins.aplana.com:8081) в автоматическом режиме