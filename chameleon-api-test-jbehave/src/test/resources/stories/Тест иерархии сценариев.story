Meta:
    @переменная_1 1
    @переменная_2 2

Narrative:

Scenario: Тест 1
GivenStories: stories/Тест 1_1.story,stories/Тест 1_2.story
When DEBUG значение "#{переменная_1}" вывести в лог

Scenario: Тест 2
GivenStories: stories/Тест 2_1.story
When DEBUG значение "#{переменная_2}" вывести в лог
