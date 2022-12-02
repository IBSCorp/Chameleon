Meta:
    @key1 HKEY_CURRENT_USER\SOFTWARE\IBS
    @key2 #{key1}\qualit
    @value1Path #{key2}\testValue1
    @value2Path #{key2}\testValue2
    @value3Path #{key2}\testValue3
    @value 123456789

Scenario: Тестирование реестра windows

When в реестре windows создан раздел "#{key1}"
When в реестре windows создан раздел "#{key2}"

When значение "#{value1Path}" в реестре windows заполнено строкой "#{value}"
When в переменную "Переменная1" сохранено значение "#{value1Path}" из реестра windows
Then значение выражения "#{Переменная1}" равно "#{value}"

When значение "#{value2Path}" в реестре windows заполнено строкой "#{value}"
When в переменную "Переменная2" сохранено значение "#{value2Path}" из реестра windows
Then значение выражения "#{Переменная2}" равно "#{value}"

When значение "#{value3Path}" в реестре windows заполнено строкой "#{value}"
When в переменную "Переменная3" сохранено значение "#{value3Path}" из реестра windows
Then значение выражения "#{Переменная3}" равно "#{value}"