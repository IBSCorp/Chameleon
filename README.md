для того чтобы опубликовать проект в репозитории необходимо выполнить команду: mvn clean deploy -DskipTests

для запуска тестов mvn clean install -pl example-selenium-cucumber -am -DforkCount=0 -U

сборка без тестов одного модуля: mvn clean install -pl qualit-selenium -DskipTests

сборка без тестов всего проекта: mvn clean install -DskipTests

mvn clean install -pl example-selenium-cucumber -am