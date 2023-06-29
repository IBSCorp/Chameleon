# language: ru

# Тестовые данные:
  # $переменная 1

Функция: Soft Assert

  Сценарий: Все провальные проверки обернуты в soft assert

    * значение выражения "#{переменная}" равно "1"

    * ~SoftAssert "Для следующего шага"
    # TODO негативная проверка CAYMAN-1254
    #* значение выражения "#{переменная}" равно "2"

    * ~SoftAssert "Включить"
    # TODO негативная проверка CAYMAN-1254
    #* значение выражения "#{переменная}" равно "3"
    #* значение выражения "#{переменная}" равно "4"
    #* значение выражения "#{переменная}" равно "5"
    * ~SoftAssert "Выключить"

  Сценарий: Часть проверкок обернута в soft assert, но затем провалилась стандартная проверка

    * значение выражения "#{переменная}" равно "1"

    * ~SoftAssert "Для следующего шага"
    # TODO негативная проверка CAYMAN-1254
    #* значение выражения "#{переменная}" равно "2"

    # TODO негативная проверка CAYMAN-1254
    #* значение выражения "#{переменная}" равно "3"